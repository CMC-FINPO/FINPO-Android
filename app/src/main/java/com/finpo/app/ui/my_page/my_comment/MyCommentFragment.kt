package com.finpo.app.ui.my_page.my_comment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentMyCommentBinding
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.my_page.MyPageViewModel
import com.finpo.app.ui.my_page.WritingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCommentFragment : BaseFragment<FragmentMyCommentBinding>(R.layout.fragment_my_comment) {
    private val viewModel by viewModels<MyPageViewModel>({requireParentFragment()})
    private lateinit var writingAdapter: WritingAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //이미 onCreate에서 데이터가 초기화 되었고 바팀 네비게이션 아이템을 클릭한 경우에만 데이터 갱신
        if(viewModel.isInitDataCompleted && (activity as MainActivity).isMovedMyPageBySelectedItem)
            viewModel.myCommentLiveData.changeMyWriting()

        writingAdapter = WritingAdapter(viewModel)
        binding.rvCommunity.adapter = writingAdapter

        viewModel.myCommentLiveData.writingList.observe(viewLifecycleOwner) {
            writingAdapter.submitList(it.toMutableList())
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<WritingContent>("writingContent")
            ?.observe(viewLifecycleOwner) { data ->
                viewModel.myCommentLiveData.checkContentChanged(data)
            }

        viewModel.myCommentLiveData.updateRecyclerViewItemEvent.observe {
            writingAdapter.notifyItemChanged(it.first, it.second)
        }
    }
}