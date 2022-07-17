package com.finpo.app.ui.onboarding.fragments

import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentOnBoarding4Binding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.ui.onboarding.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoarding4Fragment : BaseFragment<FragmentOnBoarding4Binding>(R.layout.fragment_on_boarding4) {
    private val viewModel by activityViewModels<OnBoardingViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onResume() {
        super.onResume()
        viewModel.isLastPage.value = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.isLastPage.value = false
    }
}