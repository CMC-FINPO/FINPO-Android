package com.finpo.app.ui.participation_list

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentParticipationListBinding
import com.finpo.app.ui.bookmark.InterestPolicyAdapter
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParticipationListFragment : BaseFragment<FragmentParticipationListBinding>(R.layout.fragment_participation_list) {
    private val viewModel by viewModels<ParticipationListViewModel>()
    private val args: ParticipationListFragmentArgs by navArgs()
    private lateinit var policyAdapter: ParticipationPolicyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNickname(args.nickname)
        viewModel.initData()
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getMyParticipationPolicy()

        policyAdapter = ParticipationPolicyAdapter(viewModel)
        policyAdapter.setHasStableIds(true)
        binding.rvPolicy.adapter = policyAdapter
        binding.rvPolicy.itemAnimator = null

        viewModel.policyList.observe(viewLifecycleOwner) {
            policyAdapter.submitList(it.toMutableList())
        }

        viewModel.deleteBtnClickEvent.observe {
            policyAdapter.notifyDataSetChanged()
        }

        viewModel.deleteItemClickEvent.observe { data ->
            showAlertDialog("참여한 정책을 삭제하시겠습니까?", "삭제") {
                viewModel.deleteParticipationPolicy(data)
            }
        }
    }
}