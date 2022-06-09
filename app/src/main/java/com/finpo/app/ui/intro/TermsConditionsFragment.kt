package com.finpo.app.ui.intro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentTermsConditionsBinding
import com.finpo.app.ui.common.BaseFragment

class TermsConditionsFragment : BaseFragment<FragmentTermsConditionsBinding>(R.layout.fragment_terms_conditions) {
    private val viewModel by activityViewModels<IntroViewModel>()
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.isCheckedAll.observe(viewLifecycleOwner) {
            Log.d("checked", "$it")
        }
    }
}