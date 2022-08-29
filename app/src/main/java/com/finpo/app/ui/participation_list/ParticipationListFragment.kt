package com.finpo.app.ui.participation_list

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentParticipationListBinding
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
        viewModel.getMyParticipationPolicy()
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeRecyclerView()

        observeBackEvent()
        observeGoToPolicyDetailEvent()
        observeShowBookmarkCountMaxToastEvent()

        val bottomDialogFragment = BottomSheetEditMemoDialog(viewModel)
        observeShowBottomSheetEvent(bottomDialogFragment)
        observeDismissBottomSheetEvent(bottomDialogFragment)

        observeChangeToDeleteModeEvent()
        observeDeleteItemClickEvent()
    }

    private fun observeDeleteItemClickEvent() {
        viewModel.deleteItemClickEvent.observe { data ->
            showAlertDialog("참여한 정책을 삭제하시겠습니까?", "삭제") {
                viewModel.deleteParticipationPolicy(data)
            }
        }
    }

    private fun observeChangeToDeleteModeEvent() {
        viewModel.changeToDeleteModeEvent.observe {
            policyAdapter.notifyDataSetChanged()
        }
    }

    private fun observeRecyclerView() {
        viewModel.policyList.observe(viewLifecycleOwner) {
            policyAdapter.submitList(it.toMutableList())
        }
    }

    private fun observeDismissBottomSheetEvent(bottomDialogFragment: BottomSheetEditMemoDialog) {
        viewModel.dismissBottomSheetEvent.observe {
            bottomDialogFragment.dismiss()
        }
    }

    private fun observeShowBottomSheetEvent(bottomDialogFragment: BottomSheetEditMemoDialog) {
        viewModel.showBottomSheetEvent.observe {
            bottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                bottomDialogFragment.tag
            )
        }
    }

    private fun observeShowBookmarkCountMaxToastEvent() {
        viewModel.showBookmarkCountMaxToastEvent.observe {
            longShowToast(getString(R.string.bookmark_max_msg))
        }
    }

    private fun observeGoToPolicyDetailEvent() {
        viewModel.goToPolicyDetailEvent.observe {
            findNavController().navigate(NavGraphDirections.actionGlobalPolicyDetailFragment(it))
        }
    }

    private fun observeBackEvent() {
        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }
    }

    private fun initRecyclerView() {
        policyAdapter = ParticipationPolicyAdapter(viewModel)
        binding.rvPolicy.adapter = policyAdapter
    }
}