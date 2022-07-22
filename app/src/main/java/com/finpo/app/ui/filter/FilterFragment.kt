package com.finpo.app.ui.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentFilterBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.filter.bottom_sheet.BottomSheetRegionDialog
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.format

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(R.layout.fragment_filter) {
    private val viewModel by viewModels<FilterViewModel>()
    private lateinit var filterAdapter: FilterAdapter
    private val args: FilterFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCategory()
        viewModel.setRegion(args.regionTextList.toMutableList(), args.regionIds.toList())
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

        viewModel.bottomSheetRegionViewModel.bottomFilterRegionViewModel.chooseMaxToastEvent.observe {
            shortShowToast(format(getString(R.string.can_select_max), MAX_ADDITIONAL_COUNT + 1))
        }

        viewModel.bottomSheetRegionViewModel.bottomFilterRegionViewModel.regionOverlapToastEvent.observe {
            shortShowToast(getString(R.string.overlap_region))
        }

        viewModel.goToHomeFragmentEvent.observe {
            val action = FilterFragmentDirections.actionFilterFragmentToHomeFragment(
                categories = viewModel.userCategoryData.value,
            regionIds = IntArray(viewModel.filterRegionViewModel.regionIds.size) { viewModel.filterRegionViewModel.regionIds[it] ?: 0 },
            regionTextList = viewModel.filterRegionViewModel.regionTextList.value?.toTypedArray())
            findNavController().navigate(action)
        }

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }

        viewModel.clearEvent.observe {
            showAlertDialog("초기화 하시겠어요?") {
                clearFilter()
            }
        }

        viewModel.categoryAllCheckEvent.observe {
            clearCategory()
        }

        viewModel.filterCategoryData.observe(viewLifecycleOwner) {
            filterAdapter.submitList(it)
        }
    }

    private fun clearFilter() {
        clearCategory()
        viewModel.clearRegion()
        viewModel.isCategoryAllChecked.value = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearCategory() {
        viewModel.setCategories(intArrayOf())
        filterAdapter.notifyDataSetChanged()
    }
}