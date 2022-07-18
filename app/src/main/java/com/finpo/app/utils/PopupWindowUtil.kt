package com.finpo.app.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import com.finpo.app.R
import com.finpo.app.databinding.PopupCommentBinding
import com.finpo.app.databinding.PopupCommentReplyBinding
import com.finpo.app.databinding.PopupCommunityBinding
import com.finpo.app.model.remote.CommentChilds
import com.finpo.app.model.remote.CommentContent
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.ui.community.detail.CommunityDetailViewModel
import javax.inject.Singleton

class PopupWindowUtil(val context: Context) {
    var mDropdown: PopupWindow? = null

    fun postPopupWindow(viewModel: CommunityDetailViewModel, data: WritingContent, view: View): PopupWindow? {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: PopupCommunityBinding = DataBindingUtil.inflate(inflater, R.layout.popup_community, null,true)
        binding.viewModel = viewModel
        binding.data = data

        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        mDropdown = PopupWindow(
            binding.root, 88.dp,
            FrameLayout.LayoutParams.WRAP_CONTENT, true
        )
        mDropdown?.showAsDropDown(view, -50.dp, -26.dp)

        return mDropdown
    }

    fun commentPopupWindow(viewModel: CommunityDetailViewModel, data: CommentContent, view: View): PopupWindow? {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: PopupCommentBinding = DataBindingUtil.inflate(inflater, R.layout.popup_comment, null,true)
        binding.viewModel = viewModel
        binding.data = data

        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        mDropdown = PopupWindow(
            binding.root, 78.dp,
            FrameLayout.LayoutParams.WRAP_CONTENT, true
        )
        mDropdown?.showAsDropDown(view, -50.dp, -26.dp)

        return mDropdown
    }

    fun commentReplyPopupWindow(viewModel: CommunityDetailViewModel, data: CommentChilds, view: View): PopupWindow? {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: PopupCommentReplyBinding = DataBindingUtil.inflate(inflater, R.layout.popup_comment_reply, null,true)
        binding.viewModel = viewModel
        binding.data = data

        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        mDropdown = PopupWindow(
            binding.root, 78.dp,
            FrameLayout.LayoutParams.WRAP_CONTENT, true
        )
        mDropdown?.showAsDropDown(view, -50.dp, -26.dp)

        //TODO REFACTOR
        binding.tvEditComment.setOnClickListener {
            viewModel.editComment(data.id, data.content)
            mDropdown?.dismiss()
        }

        binding.tvDeleteComment.setOnClickListener {
            viewModel.showDeleteCommentDialog(data.id)
            mDropdown?.dismiss()
        }

        binding.tvReportComment.setOnClickListener {
            viewModel.showReportDialog(COMMENT, data.id)
            mDropdown?.dismiss()
        }

        binding.tvBlockComment.setOnClickListener {
            viewModel.showBlockDialog(COMMENT, data.id)
            mDropdown?.dismiss()
        }

        return mDropdown
    }
}