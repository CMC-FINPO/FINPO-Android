package com.finpo.app.ui.community_comment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityCommentBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityCommentFragment : BaseFragment<FragmentCommunityCommentBinding>(R.layout.fragment_community_comment) {
    private val viewModel by viewModels<CommunityCommentViewModel>()
    private val args by navArgs<CommunityCommentFragmentArgs>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}