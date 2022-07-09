package com.finpo.app.ui.community

import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {
    private val viewModel by viewModels<CommunityViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


    }
}