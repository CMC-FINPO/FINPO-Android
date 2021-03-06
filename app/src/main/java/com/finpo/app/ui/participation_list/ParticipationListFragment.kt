package com.finpo.app.ui.participation_list

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentParticipationListBinding
import com.finpo.app.ui.bookmark.InterestPolicyAdapter
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.policy_detail.BottomSheetAddParticipationDialog
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
        viewModel.getMyParticipationPolicy()
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        policyAdapter = ParticipationPolicyAdapter(viewModel)
        policyAdapter.setHasStableIds(true)
        binding.rvPolicy.adapter = policyAdapter
        binding.rvPolicy.itemAnimator = null

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }

        viewModel.goToPolicyDetailEvent.observe {
            findNavController().navigate(NavGraphDirections.actionGlobalPolicyDetailFragment(it))
        }

        viewModel.updateRecyclerViewItemEvent.observe {
            policyAdapter.notifyItemChanged(it.first, it.second)
        }

        viewModel.showBookmarkCountMaxToastEvent.observe {
            longShowToast(getString(R.string.bookmark_max_msg))
        }

        val bottomDialogFragment = BottomSheetEditMemoDialog(viewModel)

        viewModel.showBottomSheetEvent.observe {
            bottomDialogFragment.show(requireActivity().supportFragmentManager, bottomDialogFragment.tag)
        }

        viewModel.dismissBottomSheetEvent.observe {
            bottomDialogFragment.dismiss()
        }

        viewModel.policyList.observe(viewLifecycleOwner) {
            policyAdapter.submitList(it.toMutableList())
        }

        viewModel.deleteBtnClickEvent.observe {
            policyAdapter.notifyDataSetChanged()
        }

        viewModel.deleteItemClickEvent.observe { data ->
            showAlertDialog("????????? ????????? ?????????????????????????", "??????") {
                viewModel.deleteParticipationPolicy(data)
            }
        }
    }
}