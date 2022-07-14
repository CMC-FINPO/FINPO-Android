package com.finpo.app.ui.community_search

import android.util.Log
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunitySearchBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.community.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunitySearchFragment : BaseFragment<FragmentCommunitySearchBinding>(R.layout.fragment_community_search) {
    private val viewModel by viewModels<CommunityViewModel>()

    override fun doViewCreated() {

    }
}