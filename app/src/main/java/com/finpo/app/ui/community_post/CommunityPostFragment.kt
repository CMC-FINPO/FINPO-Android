package com.finpo.app.ui.community_post

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityPostBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.utils.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityPostFragment : BaseFragment<FragmentCommunityPostBinding>(R.layout.fragment_community_post) {
    private val viewModel by viewModels<CommunityPostViewModel>()
    private val args by navArgs<CommunityPostFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.id = args.id
        viewModel.editTextInput.value = args.content
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }

        viewModel.showPreparationToastEvent.observe {
            shortShowToast(getString(R.string.perparation_msg))
        }

        viewModel.goToCommunityHomeFragmentEvent.observe {
            if(viewModel.id == -1)
                findNavController().navigate(CommunityPostFragmentDirections.actionCommunityPostFragmentToCommunityFragment())
            else
                findNavController().navigate(CommunityPostFragmentDirections.actionCommunityPostFragmentToCommunityDetailFragment(viewModel.id))
        }
    }
}