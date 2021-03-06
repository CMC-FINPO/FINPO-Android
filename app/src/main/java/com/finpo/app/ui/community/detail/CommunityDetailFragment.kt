package com.finpo.app.ui.community.detail

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityDetailBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.community.detail.bottom_sheet.BottomSheetReportDialog
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

        observeBackClickEvent()
        observeMoreClickEvent()
        observeDeleteItemClickEvent()

        observeDismissPopupWindow()
        observeShowBlockDialog()
        observeBlockFinishAlertDialog()
        observeShowReportFinishAlertDialog()

        val bottomDialogFragment = BottomSheetReportDialog(viewModel)
        observeShowReportDialog(bottomDialogFragment)
        observeDismissBottomSheetEvent(bottomDialogFragment)

        observeGoToCommunityCommentFragmentEvent()

        observeDeletePostClickEvent()
        observeDeleteSuccessfulEvent()

        observeEditPostClickEvent()

        initRecyclerView()
        observeRecyclerView()

        observeUpdateCommentItem()

        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        observeKeyboardHideEvent(inputMethodManager)
        observeKeyboardShowEvent(inputMethodManager)

        observeLikeBookmarkUpdateRecyclerView()
        observeBookmarkMaxToastEvent()
        observeLikeClickErrorToastEvent()

        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun observeLikeClickErrorToastEvent() {
        viewModel.likeBookmarkViewModel.likeClickErrorToastEvent.observe {
            shortShowToast(getString(R.string.cannot_like_my_post))
        }
    }

    private fun observeBookmarkMaxToastEvent() {
        viewModel.likeBookmarkViewModel.bookmarkMaxToastEvent.observe {
            shortShowToast(getString(R.string.scrap_max_msg))
        }
    }

    private fun observeLikeBookmarkUpdateRecyclerView() {
        viewModel.likeBookmarkViewModel.updateRecyclerView.observe { data ->
            writingAdapter.data = data
            writingAdapter.notifyItemChanged(0)
        }
    }

    private fun observeRecyclerView() {
        viewModel.writingContent.observe(viewLifecycleOwner) {
            writingAdapter.data = it
            writingAdapter.notifyItemChanged(0)
        }

        viewModel.commentList.observe(viewLifecycleOwner) {
            commentAdapter.submitList(it.toMutableList()) {
                if (viewModel.paging.page.value == 1)
                    binding.rvCommunityDetail.scrollToPosition(0)
            }
        }
    }

    private fun observeKeyboardShowEvent(inputMethodManager: InputMethodManager) {
        viewModel.keyBoardShowEvent.observe {
            binding.etComment.requestFocus()
            binding.etComment.postDelayed({
                inputMethodManager.showSoftInput(
                    binding.etComment,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }, 70)
        }
    }

    private fun observeKeyboardHideEvent(inputMethodManager: InputMethodManager) {
        viewModel.keyBoardHideEvent.observe {
            inputMethodManager.hideSoftInputFromWindow(binding.etComment.windowToken, 0)
        }
    }

    private fun observeUpdateCommentItem() {
        viewModel.updateCommentItem.observe {
            commentAdapter.notifyItemChanged(it.first, it.second)
        }
    }

    private fun initRecyclerView() {
        writingAdapter = WritingAdapter(viewModel)
        commentAdapter = CommentAdapter(viewModel)
        binding.rvCommunityDetail.adapter = ConcatAdapter(writingAdapter, commentAdapter)
        binding.rvCommunityDetail.itemAnimator = null
    }

    private fun observeMoreClickEvent() {
        viewModel.moreClickEvent.observe {
            postPopup = PopupWindowUtil(requireContext()).postPopupWindow(
                viewModel,
                viewModel.writingContent.value!!,
                binding.ivMore
            )
        }
    }

    private fun observeEditPostClickEvent() {
        viewModel.editPostClickEvent.observe {
            postPopup?.dismiss()
            findNavController().navigate(
                CommunityDetailFragmentDirections.actionCommunityDetailFragmentToCommunityPostFragment(
                    viewModel.detailId, viewModel.writingContent.value?.content
                )
            )
        }
    }

    private fun observeDeleteSuccessfulEvent() {
        viewModel.deleteSuccessfulEvent.observe {
            doDeleteComplete()
        }
    }

    private fun observeDeletePostClickEvent() {
        viewModel.deletePostClickEvent.observe {
            postPopup?.dismiss()
            showAlertDialog("?????? ?????????????????????????", "??????") {
                viewModel.deletePost()
            }
        }
    }

    private fun observeGoToCommunityCommentFragmentEvent() {
        viewModel.goToCommunityCommentFragmentEvent.observe { data ->
            commentAdapter.commentPopup?.dismiss()
            findNavController().navigate(
                CommunityDetailFragmentDirections.actionCommunityDetailFragmentToCommunityCommentFragment(
                    data.first,
                    data.second,
                    args.id
                )
            )
        }
    }

    private fun observeDeleteItemClickEvent() {
        viewModel.deleteItemClickEvent.observe { data ->
            commentAdapter.commentPopup?.dismiss()
            showAlertDialog("????????? ?????????????????????????", "??????") {
                viewModel.deleteComment(data)
            }
        }
    }

    private fun observeShowReportFinishAlertDialog() {
        viewModel.showReportFinishAlertDialog.observe {
            showConfirmDialog("????????? ?????????????????????!")
        }
    }

    private fun observeDismissBottomSheetEvent(bottomDialogFragment: BottomSheetReportDialog) {
        viewModel.dismissBottomSheetEvent.observe {
            bottomDialogFragment.dismiss()
        }
    }

    private fun observeShowReportDialog(bottomDialogFragment: BottomSheetReportDialog) {
        viewModel.showReportDialog.observe {
            commentAdapter.commentPopup?.dismiss()
            postPopup?.dismiss()
            bottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                bottomDialogFragment.tag
            )
        }
    }

    private fun observeBlockFinishAlertDialog() {
        viewModel.showBlockFinishAlertDialog.observe {
            showConfirmDialog("?????????????????????!") {
                doDeleteComplete()
            }
        }
    }

    private fun observeShowBlockDialog() {
        viewModel.showBlockDialog.observe {
            commentAdapter.commentPopup?.dismiss()
            postPopup?.dismiss()
            showAlertDialog("?????????????????????????", "??????") {
                viewModel.block()
            }
        }
    }

    private fun observeDismissPopupWindow() {
        viewModel.dismissPopupEvent.observe {
            commentAdapter.commentPopup?.dismiss()
            postPopup?.dismiss()
        }
    }

    private fun observeBackClickEvent() {
        viewModel.backClickEvent.observe {
            popBackStack()
        }
    }

    private fun doDeleteComplete() {
        if (!args.goToMyPage)
            findNavController().navigate(CommunityDetailFragmentDirections.actionCommunityDetailFragmentToCommunityFragment())
        else
            findNavController().navigate(CommunityDetailFragmentDirections.actionCommunityDetailFragmentToMyPageFragment())
    }

    private fun popBackStack() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "writingContent", viewModel.writingContent.value
        )
        findNavController().popBackStack()
    }
}