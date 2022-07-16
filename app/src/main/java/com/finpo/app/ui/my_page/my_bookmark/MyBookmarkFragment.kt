package com.finpo.app.ui.my_page.my_bookmark

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentMyBookmarkBinding
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.my_page.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBookmarkFragment : BaseFragment<FragmentMyBookmarkBinding>(R.layout.fragment_my_bookmark) {
    private val viewModel by viewModels<MyPageViewModel>({requireParentFragment()})
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //이미 onCreate에서 데이터가 초기화 되었고 바팀 네비게이션 아이템을 클릭한 경우에만 데이터 갱신
        if(viewModel.isInitDataCompleted && (activity as MainActivity).isMovedMyPageBySelectedItem)
            viewModel.myBookmarkLiveData.changeMyWriting()

        bookmarkAdapter = BookmarkAdapter(viewModel)
        binding.rvCommunity.adapter = bookmarkAdapter

        viewModel.myBookmarkLiveData.likeBookmarkViewModel.updateRecyclerView.observe {
            Log.d("updateRV","북마크 ${it.first}")
            bookmarkAdapter.notifyItemChanged(it.first, it.second)
        }

        viewModel.myBookmarkLiveData.likeBookmarkViewModel.likeClickErrorToastEvent.observe {
            shortShowToast(getString(R.string.cannot_like_my_post))
        }

        viewModel.myBookmarkLiveData.likeBookmarkViewModel.bookmarkMaxToastEvent.observe {
            shortShowToast(getString(R.string.bookmark_max_msg))
        }

        viewModel.myBookmarkLiveData.writingList.observe(viewLifecycleOwner) {
            bookmarkAdapter.submitList(it.toMutableList())
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<WritingContent>("writingContent")
            ?.observe(viewLifecycleOwner) { data ->
                viewModel.myBookmarkLiveData.checkContentChanged(data)
            }

        viewModel.myBookmarkLiveData.updateRecyclerViewItemEvent.observe {
            bookmarkAdapter.notifyItemChanged(it.first, it.second)
        }
    }
}