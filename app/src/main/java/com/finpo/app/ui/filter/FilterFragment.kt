package com.finpo.app.ui.filter

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentFilterBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.filter.bottom_sheet.BottomSheetRegionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(R.layout.fragment_filter) {
    private val viewModel by viewModels<FilterViewModel>()
    private lateinit var filterAdapter: FilterAdapter
    private val args: FilterFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCategory()
        viewModel.setRegion(args.regions.toMutableList())
        viewModel.setCategories(args.categories)
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        filterAdapter = FilterAdapter(viewModel)
        filterAdapter.setHasStableIds(true)
        binding.rvFilter.adapter = filterAdapter
        binding.rvFilter.itemAnimator = null

        val bottomDialogFragment = BottomSheetRegionDialog(viewModel)
        viewModel.showBottomSheetEvent.observe {
            bottomDialogFragment.show(requireActivity().supportFragmentManager, bottomDialogFragment.tag)
        }

        viewModel.dismissBottomSheetEvent.observe {
            bottomDialogFragment.dismiss()
        }

        viewModel.bottomSheetRegionViewModel.chooseMaxToastEvent.observe {
            shortShowToast("최대 6개까지 선택할 수 있어요!")
        }

        viewModel.bottomSheetRegionViewModel.regionOverlapToastEvent.observe {
            shortShowToast("이미 선택된 지역입니다!")
        }

        viewModel.goToHomeFragmentEvent.observe {
            val action = FilterFragmentDirections.actionFilterFragmentToHomeFragment(
                viewModel.userCategoryData.value ?: intArrayOf(),
            IntArray(viewModel.filterRegionSelCount.value ?: 0) { viewModel.filterRegionSelTextList.value?.get(it)?.id ?: 0 },
            viewModel.filterRegionSelTextList.value?.toTypedArray() ?: arrayOf())
            findNavController().navigate(action)
        }

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }

        viewModel.clearEvent.observe {
            showAlertDialog("초기화", "초기화 하시겠어요?") {
                clearFilter()
            }
        }

        viewModel.filterCategoryData.observe(viewLifecycleOwner) {
            filterAdapter.submitList(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearFilter() {
        viewModel.setCategories(intArrayOf())
        filterAdapter.notifyDataSetChanged()
        viewModel.clearRegion()
    }
}