package com.finpo.app.ui.intro.additional_region

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentAdditionalRegionBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.ui.intro.living_area.RegionAdapter
import com.finpo.app.ui.intro.living_area.RegionDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalRegionFragment : BaseFragment<FragmentAdditionalRegionBinding>(R.layout.fragment_additional_region) {
    private val viewModel by activityViewModels<IntroViewModel>()
    @Inject lateinit var additionalRegionAdapter: AdditionalRegionAdapter
    @Inject lateinit var additionalRegionDetailAdapter: AdditionalRegionDetailAdapter

    override fun doCreateView() {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvAdditionalRegionAll.adapter = additionalRegionAdapter
        additionalRegionDetailAdapter.setHasStableIds(true)
        binding.rvAdditionalRegionDetail.adapter = additionalRegionDetailAdapter
        binding.rvAdditionalRegionDetail.itemAnimator = null

        viewModel.additionalRegionLiveData.additionalRegionData.observe(viewLifecycleOwner) {
            additionalRegionAdapter.submitList(it.data)
        }

        viewModel.additionalRegionLiveData.additionalRegionSelEvent.observe {
            additionalRegionAdapter.notifyDataSetChanged()
        }

        viewModel.additionalRegionLiveData.additionalRegionDetailData.observe(viewLifecycleOwner) {
            additionalRegionDetailAdapter.submitList(it.data)
        }

        viewModel.additionalRegionLiveData.chooseMaxToastEvent.observe {
            shortShowToast(getString(R.string.can_select_max))
        }

        viewModel.additionalRegionLiveData.regionOverlapToastEvent.observe {
            shortShowToast(getString(R.string.overlap_region))
        }
    }
}