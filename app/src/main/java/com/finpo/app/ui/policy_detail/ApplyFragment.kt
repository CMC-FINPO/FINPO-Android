package com.finpo.app.ui.policy_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentApplyBinding
import com.finpo.app.ui.common.BaseFragment

class ApplyFragment : BaseFragment<FragmentApplyBinding>(R.layout.fragment_apply) {
    private val viewModel by viewModels<PolicyDetailViewModel>({requireParentFragment()})
    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}