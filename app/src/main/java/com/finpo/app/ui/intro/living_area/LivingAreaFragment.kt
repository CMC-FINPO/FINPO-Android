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
import javax.inject.Inject

@AndroidEntryPoint
class LivingAreaFragment : BaseFragment<FragmentLivingAreaBinding>(R.layout.fragment_living_area) {
    private val viewModel by activityViewModels<IntroViewModel>()
    @Inject lateinit var regionAdapter: RegionAdapter
    @Inject lateinit var regionDetailAdapter: RegionDetailAdapter
    @SuppressLint("NotifyDataSetChanged")
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvRegionAll.adapter = regionAdapter

        regionDetailAdapter.setHasStableIds(true)
        binding.rvRegionDetail.adapter = regionDetailAdapter
        binding.rvRegionDetail.itemAnimator = null

        viewModel.livingAreaLiveData.regionData.observe(viewLifecycleOwner) {
            regionAdapter.submitList(it.data)
        }

        viewModel.livingAreaLiveData.regionDetailData.observe(viewLifecycleOwner) {
            regionDetailAdapter.submitList(it.data)
        }

        viewModel.livingAreaLiveData.regionSelEvent.observe { regionId ->
            regionAdapter.notifyDataSetChanged()
            viewModel.livingAreaLiveData.getRegionDetail(regionId)
        }

        viewModel.livingAreaLiveData.showRegionToastEvent.observe {
            shortShowToast(getString(R.string.show_only_one_living_area))
        }
    }
}