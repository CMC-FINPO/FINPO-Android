package com.finpo.app.ui.community.post

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("postTextStyle")
fun postTextStyle(textView: TextView, text: String?) {
    if(text.isNullOrEmpty()) {
        textView.isClickable = false
        textView.setTextColor(textView.context.getColor(R.color.gray_g05))
    } else {
        textView.isClickable = true
        textView.setTextColor(textView.context.getColor(R.color.point_p01))
    }
}