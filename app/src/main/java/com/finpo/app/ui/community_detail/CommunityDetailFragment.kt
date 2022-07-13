package com.finpo.app.ui.community_detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityDetailBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.community_detail.bottom_sheet.BottomSheetReportDialog
import com.finpo.app.ui.filter.bottom_sheet.BottomSheetRegionDialog
import com.finpo.app.utils.PopupWindowUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityDetailFragment :
    BaseFragment<FragmentCommunityDetailBinding>(R.layout.fragment_community_detail) {
    private val viewModel by viewModels<CommunityDetailViewModel>()
    private val args by navArgs<CommunityDetailFragmentArgs>()

    private lateinit var writingAdapter: WritingAdapter
    private lateinit var commentAdapter: CommentAdapter

    private var postPopup: PopupWindow? = null

    val TAG = "CommunityDetailFragment"

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.detailId = args.id
        viewModel.getInitData()
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val bottomDialogFragment = BottomSheetReportDialog(viewModel)

        viewModel.showReportDialog.observe {
            commentAdapter.commentPopup?.dismiss()
            postPopup?.dismiss()
            bottomDialogFragment.show(requireActivity().supportFragmentManager, bottomDialogFragment.tag)
        }

        viewModel.dismissBottomSheetEvent.observe {
            bottomDialogFragment.dismiss()
        }

        viewModel.showReportFinishAlertDialog.observe {
            showConfirmDialog("신고가 접수되었습니다!")
        }

        viewModel.deleteItemClickEvent.observe { data ->
            commentAdapter.commentPopup?.dismiss()
            showAlertDialog("댓글을 삭제하시겠습니까?", "삭제") {
                viewModel.deleteComment(data)
            }
        }

        viewModel.goToCommunityCommentFragmentEvent.observe { data ->
            commentAdapter.commentPopup?.dismiss()
            findNavController().navigate(CommunityDetailFragmentDirections.actionCommunityDetailFragmentToCommunityCommentFragment(data.id, data.content ?: "", args.id))
        }

        viewModel.deletePostClickEvent.observe {
            postPopup?.dismiss()
            showAlertDialog("글을 삭제하시겠습니까?", "삭제") {
                viewModel.deletePost()
            }
        }

        viewModel.goToCommunityHomeFragmentEvent.observe {
            findNavController().navigate(CommunityDetailFragmentDirections.actionCommunityDetailFragmentToCommunityFragment())
        }

        viewModel.editPostClickEvent.observe {
            postPopup?.dismiss()
            findNavController().navigate(
                CommunityDetailFragmentDirections.actionCommunityDetailFragmentToCommunityPostFragment(
                    viewModel.detailId, viewModel.writingContent.value?.content
                )
            )
        }

        viewModel.moreClickEvent.observe {
            postPopup = PopupWindowUtil(requireContext()).postPopupWindow(
                viewModel,
                viewModel.writingContent.value!!,
                binding.ivMore
            )
        }

        writingAdapter = WritingAdapter()
        commentAdapter = CommentAdapter(viewModel)
        binding.rvCommunityDetail.adapter = ConcatAdapter(writingAdapter, commentAdapter)
        binding.rvCommunityDetail.itemAnimator = null

        viewModel.keyBoardHideEvent.observe {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etComment.windowToken, 0)
        }


        viewModel.writingContent.observe(viewLifecycleOwner) {
            writingAdapter.data = it
            writingAdapter.notifyItemChanged(0)
        }

        viewModel.commentList.observe(viewLifecycleOwner) {
            commentAdapter.submitList(it.toMutableList()) {
                if(viewModel.paging.page.value == 1)
                    binding.rvCommunityDetail.scrollToPosition(0)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun popBackStack() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "writingContent", viewModel.writingContent.value
        )
        findNavController().popBackStack()
    }
}