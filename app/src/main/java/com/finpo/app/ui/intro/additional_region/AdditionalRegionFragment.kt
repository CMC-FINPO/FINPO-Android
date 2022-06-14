package com.finpo.app.ui.intro.additional_region

import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentAdditionalRegionBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.ui.intro.living_area.RegionAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdditionalRegionFragment : BaseFragment<FragmentAdditionalRegionBinding>(R.layout.fragment_additional_region) {
    private val viewModel by activityViewModels<IntroViewModel>()
    @Inject lateinit var additionalRegionAdapter: AdditionalRegionAdapter
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvAdditionalRegionAll.adapter = additionalRegionAdapter

        viewModel.additionalRegionLiveData.additionalRegionData.observe(viewLifecycleOwner) {
            additionalRegionAdapter.submitList(it.data)
        }
    }
}