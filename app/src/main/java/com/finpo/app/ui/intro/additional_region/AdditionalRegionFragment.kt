package com.finpo.app.ui.intro.additional_region

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentAdditionalRegionBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.ui.intro.living_area.RegionAdapter
import com.finpo.app.ui.intro.living_area.RegionDetailAdapter
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.format
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalRegionFragment : BaseFragment<FragmentAdditionalRegionBinding>(R.layout.fragment_additional_region) {
    private val viewModel by activityViewModels<IntroViewModel>()
    @Inject lateinit var additionalRegionAdapter: AdditionalRegionAdapter
    @Inject lateinit var additionalRegionDetailAdapter: AdditionalRegionDetailAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setRecyclerView()
        observeRecyclerViewData()
        observeRecyclerViewEvent()
    }

    private fun observeRecyclerViewEvent() {
        viewModel.additionalRegionLiveData.additionalRegionRegionViewModel.chooseMaxToastEvent.observe {
            shortShowToast(format(getString(R.string.can_select_max), MAX_ADDITIONAL_COUNT))
        }

        viewModel.additionalRegionLiveData.additionalRegionRegionViewModel.regionOverlapToastEvent.observe {
            shortShowToast(getString(R.string.overlap_region))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRecyclerViewData() {
        viewModel.additionalRegionLiveData.additionalRegionRegionViewModel.regionData.observe(viewLifecycleOwner) {
            additionalRegionAdapter.submitList(it.data)
        }

        viewModel.additionalRegionLiveData.additionalRegionRegionViewModel.regionDetailData.observe(viewLifecycleOwner) {
            additionalRegionDetailAdapter.submitList(it.data)
            additionalRegionAdapter.notifyDataSetChanged()
        }
    }

    private fun setRecyclerView() {
        additionalRegionAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionAll.adapter = additionalRegionAdapter
        binding.regionRecyclerview.rvRegionAll.itemAnimator = null

        additionalRegionDetailAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionDetail.adapter = additionalRegionDetailAdapter
        binding.regionRecyclerview.rvRegionDetail.itemAnimator = null
    }
}