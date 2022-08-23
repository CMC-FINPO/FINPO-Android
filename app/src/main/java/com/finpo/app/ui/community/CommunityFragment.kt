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
        viewModel.initData()
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeRecyclerView()

        observeSearchClickEvent()
        observeGoToDetailFragmentEvent()
        observeGoToPostFragmentEvent()

        val bottomDialogFragment = BottomSheetCommunitySortDialog(viewModel)
        observeBottomSheetShowEvent(bottomDialogFragment)
        observeBottomSheetDismissEvent(bottomDialogFragment)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<WritingContent>("writingContent")
            ?.observe(viewLifecycleOwner) { data ->
               viewModel.checkContentChanged(data)
            }

        viewModel.likeBookmarkViewModel.updateRecyclerView.observe { data ->
            viewModel.checkContentChanged(data)
        }

        observeLikeClickErrorToastEvent()
        observeBookmarkMaxToastEvent()
    }

    private fun observeBookmarkMaxToastEvent() {
        viewModel.likeBookmarkViewModel.bookmarkMaxToastEvent.observe {
            shortShowToast(getString(R.string.scrap_max_msg))
        }
    }

    private fun observeLikeClickErrorToastEvent() {
        viewModel.likeBookmarkViewModel.likeClickErrorToastEvent.observe {
            shortShowToast(getString(R.string.cannot_like_my_post))
        }
    }

    private fun observeBottomSheetDismissEvent(bottomDialogFragment: BottomSheetCommunitySortDialog) {
        viewModel.bottomSheetDismissEvent.observe {
            bottomDialogFragment.dismiss()
        }
    }

    private fun observeBottomSheetShowEvent(bottomDialogFragment: BottomSheetCommunitySortDialog) {
        viewModel.bottomSheetShowEvent.observe {
            bottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                bottomDialogFragment.tag
            )
        }
    }

    private fun observeGoToPostFragmentEvent() {
        viewModel.goToPostFragmentEvent.observe {
            findNavController().navigate(CommunityFragmentDirections.actionCommunityFragmentToCommunityPostFragment())
        }
    }

    private fun observeGoToDetailFragmentEvent() {
        viewModel.goToDetailFragmentEvent.observe {
            findNavController().navigate(NavGraphDirections.actionGlobalCommunityDetailFragment(it))
        }
    }

    private fun observeSearchClickEvent() {
        viewModel.searchClickEvent.observe {
            findNavController().navigate(CommunityFragmentDirections.actionCommunityFragmentToCommunitySearchFragment())
        }
    }

    private fun observeRecyclerView() {
        viewModel.writingList.observe(viewLifecycleOwner) {
            communityAdapter.submitList(it.toMutableList()) {
                if (viewModel.paging.page.value == 1 && !viewModel.refreshedByContentChanged)
                    binding.rvCommunity.scrollToPosition(0)
                viewModel.refreshedByContentChanged = false
            }
        }
    }

    private fun initRecyclerView() {
        communityAdapter = CommunityAdapter(viewModel)
        binding.rvCommunity.adapter = communityAdapter
    }
}