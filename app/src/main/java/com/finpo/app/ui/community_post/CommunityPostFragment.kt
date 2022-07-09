package com.finpo.app.ui.community_post

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityPostBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.utils.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityPostFragment : BaseFragment<FragmentCommunityPostBinding>(R.layout.fragment_community_post) {
    private val viewModel by viewModels<CommunityPostViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}