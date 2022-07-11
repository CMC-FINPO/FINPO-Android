package com.finpo.app.ui.community_detail

import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityDetailBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityDetailFragment : BaseFragment<FragmentCommunityDetailBinding>(R.layout.fragment_community_detail) {
    private val viewModel by viewModels<CommunityDetailViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}