package com.finpo.app.ui.community.comment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityCommentBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityCommentFragment : BaseFragment<FragmentCommunityCommentBinding>(R.layout.fragment_community_comment) {
    private val viewModel by viewModels<CommunityCommentViewModel>()
    private val args by navArgs<CommunityCommentFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.commentId = args.commentId
        viewModel.editTextInput.value = args.content
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.goToCommunityDetailFragmentEvent.observe {
            findNavController().navigate(CommunityCommentFragmentDirections.actionCommunityCommentFragmentToCommunityDetailFragment(args.postId))
        }

        viewModel.backClick.observe {
            findNavController().popBackStack()
        }
    }
}