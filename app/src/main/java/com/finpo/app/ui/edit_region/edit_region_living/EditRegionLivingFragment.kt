package com.finpo.app.ui.edit_region.edit_region_living

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentEditRegionLivingBinding
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.edit_region.EditRegionViewModel
import com.finpo.app.ui.edit_region.edit_region_interest.EditInterestRegionAdapter
import com.finpo.app.ui.edit_region.edit_region_interest.EditInterestRegionDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.format

@AndroidEntryPoint
class EditRegionLivingFragment : BaseFragment<FragmentEditRegionLivingBinding>(R.layout.fragment_edit_region_living) {
    private val viewModel by viewModels<EditRegionViewModel>({requireParentFragment()})
    private lateinit var regionAllAdapter: EditLivingRegionAdapter
    private lateinit var regionDetailAdapter: EditLivingRegionDetailAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initData()
        initRecyclerView()
        observeRecyclerView()
        observeToastViewModel()
    }

    private fun observeToastViewModel() {
        viewModel.livingRegionViewModel.chooseMaxToastEvent.observe {
            shortShowToast(format(getString(R.string.can_select_max), 1))
        }

        viewModel.livingRegionViewModel.regionOverlapToastEvent.observe {
            shortShowToast(getString(R.string.overlap_region))
        }
    }

    private fun initData() {
        viewModel.livingRegionViewModel.getRegionDataByLocal(
            viewModel.interestRegionViewModel.regionData.value?.copy() ?: RegionResponse(listOf())
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRecyclerView() {
        viewModel.livingRegionViewModel.regionData.observe(viewLifecycleOwner) {
            regionAllAdapter.submitList(it.data)
        }

        viewModel.livingRegionViewModel.regionDetailData.observe(viewLifecycleOwner) {
            regionDetailAdapter.submitList(it.data)
            regionAllAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        regionAllAdapter = EditLivingRegionAdapter(viewModel)
        regionAllAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionAll.adapter = regionAllAdapter
        binding.regionRecyclerview.rvRegionAll.itemAnimator = null

        regionDetailAdapter = EditLivingRegionDetailAdapter(viewModel)
        regionDetailAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionDetail.adapter = regionDetailAdapter
        binding.regionRecyclerview.rvRegionDetail.itemAnimator = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.setViewpagerTypeLiving()
    }
}