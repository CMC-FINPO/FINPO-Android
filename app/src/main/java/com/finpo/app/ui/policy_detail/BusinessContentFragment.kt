package com.finpo.app.ui.policy_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentBusinessContentBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessContentFragment : BaseFragment<FragmentBusinessContentBinding>(R.layout.fragment_business_content) {
    private val viewModel by viewModels<PolicyDetailViewModel>({requireParentFragment()})
    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}