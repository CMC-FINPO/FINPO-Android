package com.finpo.app.ui.intro.living_area

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.finpo.app.R
import com.finpo.app.databinding.FragmentLivingAreaBinding
import com.finpo.app.model.remote.Region
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.format
import javax.inject.Inject

@AndroidEntryPoint
class LivingAreaFragment : BaseFragment<FragmentLivingAreaBinding>(R.layout.fragment_living_area) {
    private val viewModel by activityViewModels<IntroViewModel>()
    @Inject lateinit var regionAdapter: RegionAdapter
    @Inject lateinit var regionDetailAdapter: RegionDetailAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setRecyclerView()
        observeRecyclerViewData()
        observeRecyclerViewEvent()
    }

    private fun setRecyclerView() {
        regionAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionAll.adapter = regionAdapter
        binding.regionRecyclerview.rvRegionAll.itemAnimator = null

        regionDetailAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionDetail.adapter = regionDetailAdapter
        binding.regionRecyclerview.rvRegionDetail.itemAnimator = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRecyclerViewData() {
        viewModel.livingAreaLiveData.livingRegionViewModel.regionData.observe(viewLifecycleOwner) {
            regionAdapter.submitList(it.data)
        }

        viewModel.livingAreaLiveData.livingRegionViewModel.regionDetailData.observe(viewLifecycleOwner) {
            regionDetailAdapter.submitList(it.data)
            regionAdapter.notifyDataSetChanged()
        }
    }

    private fun observeRecyclerViewEvent() {
        viewModel.livingAreaLiveData.livingRegionViewModel.chooseMaxToastEvent.observe {
            shortShowToast(getString(R.string.show_only_one_living_area))
        }
    }
}