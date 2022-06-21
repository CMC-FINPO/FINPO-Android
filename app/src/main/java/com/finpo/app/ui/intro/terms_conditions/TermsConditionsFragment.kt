package com.finpo.app.ui.intro.terms_conditions

import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentTermsConditionsBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsConditionsFragment : BaseFragment<FragmentTermsConditionsBinding>(R.layout.fragment_terms_conditions) {
    private val viewModel by activityViewModels<IntroViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.termsConditionsLiveData.checkedAllClickEvent.observe { check ->
            viewModel.termsConditionsLiveData.changeOtherCheckBox(check)
        }
    }
}