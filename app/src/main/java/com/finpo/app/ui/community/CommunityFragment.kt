package com.finpo.app.ui.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityBinding
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.home.BottomSheetPolicySortDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {
    private val viewModel by viewModels<CommunityViewModel>()
    private lateinit var communityAdapter: CommunityAdapter
    val TAG = "CommunityFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initData() // CommunitySearchFragment 애서는 데이터 초기화를 진행하지 않기 위해 initData를 onCreate에서 실행함
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        communityAdapter = CommunityAdapter(viewModel)
        binding.rvCommunity.adapter = communityAdapter

        viewModel.writingList.observe(viewLifecycleOwner) {
            communityAdapter.submitList(it.toMutableList()) {
                if(viewModel.paging.page.value == 1)
                    binding.rvCommunity.scrollToPosition(0)
            }
        }

        if((activity as MainActivity).isMovedCommunityBySelectedItem) {
            viewModel.clearWriting()
            viewModel.changeWriting()
        }

        viewModel.searchClickEvent.observe {
            findNavController().navigate(CommunityFragmentDirections.actionCommunityFragmentToCommunitySearchFragment())
        }

        viewModel.goToDetailFragmentEvent.observe {
            findNavController().navigate(NavGraphDirections.actionGlobalCommunityDetailFragment(it))
        }

        val bottomDialogFragment = BottomSheetCommunitySortDialog(viewModel)

        viewModel.bottomSheetShowEvent.observe {
            bottomDialogFragment.show(requireActivity().supportFragmentManager, bottomDialogFragment.tag)
        }

        viewModel.bottomSheetDismissEvent.observe {
            bottomDialogFragment.dismiss()
        }

        viewModel.goToPostFragmentEvent.observe {
            findNavController().navigate(CommunityFragmentDirections.actionCommunityFragmentToCommunityPostFragment())
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<WritingContent>("writingContent")
            ?.observe(viewLifecycleOwner) { data ->
               viewModel.checkContentChanged(data)
            }

        viewModel.updateRecyclerViewItemEvent.observe {
            communityAdapter.notifyItemChanged(it.first, it.second)
        }
    }
}