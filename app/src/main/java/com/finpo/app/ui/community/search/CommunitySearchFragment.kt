package com.finpo.app.ui.community.search

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunitySearchBinding
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.community.CommunityAdapter
import com.finpo.app.ui.community.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunitySearchFragment : BaseFragment<FragmentCommunitySearchBinding>(R.layout.fragment_community_search) {
    private val viewModel by viewModels<CommunityViewModel>()
    private lateinit var communityAdapter: CommunityAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeRecyclerView()

        observeGoToDetailFragmentEvent()
        observeBackClickEvent()
        observeKeyBoardSearchEvent()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<WritingContent>("writingContent")
            ?.observe(viewLifecycleOwner) { data ->
                viewModel.checkContentChanged(data)
            }

        viewModel.likeBookmarkViewModel.bookmarkMaxToastEvent.observe {
            shortShowToast(getString(R.string.bookmark_max_msg))
        }

        viewModel.likeBookmarkViewModel.likeClickErrorToastEvent.observe {
            shortShowToast(getString(R.string.cannot_like_my_post))
        }

        viewModel.likeBookmarkViewModel.updateRecyclerView.observe { data ->
            viewModel.checkContentChanged(data)
        }

        viewModel.updateRecyclerViewItemEvent.observe {
            communityAdapter.notifyItemChanged(it.first, it.second)
        }
    }

    private fun observeKeyBoardSearchEvent() {
        viewModel.searchLiveData.keyBoardSearchEvent.observe {
            viewModel.changeWriting()
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }
    }

    private fun observeBackClickEvent() {
        viewModel.backClickEvent.observe {
            findNavController().popBackStack()
        }
    }

    private fun observeGoToDetailFragmentEvent() {
        viewModel.goToDetailFragmentEvent.observe {
            findNavController().navigate(NavGraphDirections.actionGlobalCommunityDetailFragment(it))
        }
    }

    private fun observeRecyclerView() {
        viewModel.writingList.observe(viewLifecycleOwner) {
            communityAdapter.submitList(it.toMutableList()) {
                if (viewModel.paging.page.value == 1)
                    binding.rvCommunity.scrollToPosition(0)
            }
        }
    }

    private fun initRecyclerView() {
        communityAdapter = CommunityAdapter(viewModel)
        binding.rvCommunity.adapter = communityAdapter
    }
}