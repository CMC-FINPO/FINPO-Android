package com.finpo.app.utils

import android.R.layout
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import com.finpo.app.R
import com.finpo.app.databinding.PopupCommentBinding
import com.finpo.app.databinding.PopupCommunityBinding
import com.finpo.app.model.remote.CommentContent
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.ui.community_detail.CommunityDetailViewModel


class PopupWindowUtil(val context: Context) {
    fun postPopupWindow(viewModel: CommunityDetailViewModel, data: WritingContent, view: View): PopupWindow {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: PopupCommunityBinding = DataBindingUtil.inflate(inflater, R.layout.popup_community, null,true)
        binding.viewModel = viewModel
        binding.data = data

        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val mDropdown = PopupWindow(
            binding.root, 88.dp,
            FrameLayout.LayoutParams.WRAP_CONTENT, true
        )
        mDropdown.showAsDropDown(view, -50.dp, -26.dp)

        return mDropdown
    }

    fun commentPopupWindow(viewModel: CommunityDetailViewModel, data: CommentContent, view: View): PopupWindow {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: PopupCommentBinding = DataBindingUtil.inflate(inflater, R.layout.popup_comment, null,true)
        binding.viewModel = viewModel
        binding.data = data

        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val mDropdown = PopupWindow(
            binding.root, 78.dp,
            FrameLayout.LayoutParams.WRAP_CONTENT, true
        )
        mDropdown.showAsDropDown(view, -50.dp, -26.dp)

        return mDropdown
    }
}