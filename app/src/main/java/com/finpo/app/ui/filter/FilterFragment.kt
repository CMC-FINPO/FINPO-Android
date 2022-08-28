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

        initRecyclerView()
        observeRecyclerView()

        val bottomDialogFragment = BottomSheetRegionDialog(viewModel)
        observeShowBottomSheetEvent(bottomDialogFragment)
        observeDismissBottomSheetEvent(bottomDialogFragment)

        observeChooseMaxToastEvent()
        observeRegionOverlapToastEvent()

        observeGoToHomeFragmentEvent()
        observeBackEvent()
        observeClearEvent()
        observeCategoryAllCheckEvent()
    }

    private fun observeCategoryAllCheckEvent() {
        viewModel.categoryAllCheckEvent.observe {
            clearCategory()
        }
    }

    private fun observeClearEvent() {
        viewModel.clearEvent.observe {
            showAlertDialog("초기화 하시겠어요?") {
                clearFilter()
            }
        }
    }

    private fun observeBackEvent() {
        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }
    }

    private fun observeGoToHomeFragmentEvent() {
        viewModel.goToHomeFragmentEvent.observe {
            val action = FilterFragmentDirections.actionFilterFragmentToHomeFragment(
                categories = viewModel.userCategoryData.value,
                regionIds = IntArray(viewModel.filterRegionViewModel.regionIds.size) {
                    viewModel.filterRegionViewModel.regionIds[it] ?: 0
                },
                regionTextList = viewModel.filterRegionViewModel.regionTextList.value?.toTypedArray()
            )
            findNavController().navigate(action)
        }
    }

    private fun observeRegionOverlapToastEvent() {
        viewModel.bottomSheetRegionViewModel.bottomFilterRegionViewModel.regionOverlapToastEvent.observe {
            shortShowToast(getString(R.string.overlap_region))
        }
    }

    private fun observeChooseMaxToastEvent() {
        viewModel.bottomSheetRegionViewModel.bottomFilterRegionViewModel.chooseMaxToastEvent.observe {
            shortShowToast(format(getString(R.string.can_select_max), MAX_ADDITIONAL_COUNT + 1))
        }
    }

    private fun observeDismissBottomSheetEvent(bottomDialogFragment: BottomSheetRegionDialog) {
        viewModel.dismissBottomSheetEvent.observe {
            bottomDialogFragment.dismiss()
        }
    }

    private fun observeShowBottomSheetEvent(bottomDialogFragment: BottomSheetRegionDialog) {
        viewModel.showBottomSheetEvent.observe {
            bottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                bottomDialogFragment.tag
            )
        }
    }

    private fun observeRecyclerView() {
        viewModel.filterCategoryData.observe(viewLifecycleOwner) {
            filterAdapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        filterAdapter = FilterAdapter(viewModel)
        filterAdapter.setHasStableIds(true)
        binding.rvFilter.adapter = filterAdapter
        binding.rvFilter.itemAnimator = null
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