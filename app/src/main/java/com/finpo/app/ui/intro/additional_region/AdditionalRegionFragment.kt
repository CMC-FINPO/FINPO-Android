package com.finpo.app.ui.intro.additional_region

import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentAdditionalRegionBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel

class AdditionalRegionFragment : BaseFragment<FragmentAdditionalRegionBinding>(R.layout.fragment_additional_region) {
    private val viewModel by activityViewModels<IntroViewModel>()
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}