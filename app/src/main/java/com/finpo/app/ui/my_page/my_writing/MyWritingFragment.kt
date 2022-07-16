package com.finpo.app.ui.my_page.my_writing

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentMyWritingBinding
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.my_page.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWritingFragment : BaseFragment<FragmentMyWritingBinding>(R.layout.fragment_my_writing) {
    private val viewModel by viewModels<MyPageViewModel>({requireParentFragment()})
    private lateinit var writingAdapter: WritingAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //이미 onCreate에서 데이터가 초기화 되었고 바팀 네비게이션 아이템을 클릭한 경우에만 데이터 갱신
        if(viewModel.isInitDataCompleted && (activity as MainActivity).isMovedMyPageBySelectedItem)
            viewModel.myWritingLiveData.changeMyWriting()

        writingAdapter = WritingAdapter(viewModel)
        binding.rvCommunity.adapter = writingAdapter

        viewModel.myWritingLiveData.writingList.observe(viewLifecycleOwner) {
            writingAdapter.submitList(it.toMutableList()) {
                if(viewModel.myWritingLiveData.paging.page.value == 1)
                    binding.rvCommunity.scrollToPosition(0)
            }
        }

        viewModel.myWritingLiveData.likeBookmarkViewModel.likeClickErrorToastEvent.observe {
            shortShowToast(getString(R.string.cannot_like_my_post))
        }

        viewModel.myWritingLiveData.likeBookmarkViewModel.updateRecyclerView.observe {
            Log.d("updateRV","내가 쓴 글 ${it.first}")
            writingAdapter.notifyItemChanged(it.first, it.second)
        }

        viewModel.myWritingLiveData.likeBookmarkViewModel.bookmarkMaxToastEvent.observe {
            shortShowToast(getString(R.string.bookmark_max_msg))
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<WritingContent>("writingContent")
            ?.observe(viewLifecycleOwner) { data ->
                viewModel.myWritingLiveData.checkContentChanged(data)
            }

        viewModel.myWritingLiveData.updateRecyclerViewItemEvent.observe {
            writingAdapter.notifyItemChanged(it.first, it.second)
        }
    }
}