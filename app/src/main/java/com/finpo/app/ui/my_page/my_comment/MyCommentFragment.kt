package com.finpo.app.ui.my_page.my_comment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentMyCommentBinding
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.my_page.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCommentFragment : BaseFragment<FragmentMyCommentBinding>(R.layout.fragment_my_comment) {
    private val viewModel by viewModels<MyPageViewModel>({requireParentFragment()})
    private lateinit var commentAdapter: CommentAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeRecyclerView()

        //이미 onCreate에서 데이터가 초기화 되었고 바팀 네비게이션 아이템을 클릭한 경우에만 데이터 갱신
        if(viewModel.isInitDataCompleted && (activity as MainActivity).isMovedMyPageBySelectedBottomNavigationItem)
            viewModel.myCommentLiveData.changeMyComment()

        observeLikeClickErrorToastEvent()
        observeUpdateRecyclerView()
        observeBookmarkMaxToastEvent()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<WritingContent>("writingContent")
            ?.observe(viewLifecycleOwner) { data ->
                viewModel.myCommentLiveData.checkContentChanged(data)
            }
    }

    private fun observeRecyclerView() {
        viewModel.myCommentLiveData.writingList.observe(viewLifecycleOwner) {
            commentAdapter.submitList(it.toMutableList()) {
                if (viewModel.myCommentLiveData.paging.page.value == 1)
                    binding.rvCommunity.scrollToPosition(0)
            }
        }
    }

    private fun observeBookmarkMaxToastEvent() {
        viewModel.myCommentLiveData.likeBookmarkViewModel.bookmarkMaxToastEvent.observe {
            shortShowToast(getString(R.string.scrap_max_msg))
        }
    }

    private fun observeUpdateRecyclerView() {
        viewModel.myCommentLiveData.likeBookmarkViewModel.updateRecyclerView.observe { data ->
            viewModel.myCommentLiveData.checkContentChanged(data)
        }
    }

    private fun observeLikeClickErrorToastEvent() {
        viewModel.myCommentLiveData.likeBookmarkViewModel.likeClickErrorToastEvent.observe {
            shortShowToast(getString(R.string.cannot_like_my_post))
        }
    }

    private fun initRecyclerView() {
        commentAdapter = CommentAdapter(viewModel)
        binding.rvCommunity.adapter = commentAdapter
    }
}