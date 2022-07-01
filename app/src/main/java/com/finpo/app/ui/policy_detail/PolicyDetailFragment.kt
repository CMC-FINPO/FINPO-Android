package com.finpo.app.ui.policy_detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentPolicyDetailBinding
import com.finpo.app.ui.common.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyDetailFragment : BaseFragment<FragmentPolicyDetailBinding>(R.layout.fragment_policy_detail) {
    private val viewModel by viewModels<PolicyDetailViewModel>()
    private val args: PolicyDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPolicyDetail(args.id)
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewPagerPolicyDetail.adapter = PolicyDetailViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tbPolicyDetail, binding.viewPagerPolicyDetail) {  tab, position ->
            tab.text = when(position) {
                0 -> "사업 내용"
                else -> "신청 방법"
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}