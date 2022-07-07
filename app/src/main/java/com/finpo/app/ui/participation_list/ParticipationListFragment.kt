package com.finpo.app.ui.participation_list

import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentParticipationListBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipationListFragment : BaseFragment<FragmentParticipationListBinding>(R.layout.fragment_participation_list) {
    private val viewModel by viewModels<ParticipationListViewModel>()
    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}