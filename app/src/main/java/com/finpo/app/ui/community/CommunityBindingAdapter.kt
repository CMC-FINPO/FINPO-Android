package com.finpo.app.ui.community

import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("setAnonymousTextColor")
fun setFont(
    textView: TextView,
    isAnonymous: Boolean
) {
    if(isAnonymous) {
        textView.setTextColor(textView.context.getColor(R.color.point_p01))
    }
    else {
        textView.setTextColor(textView.context.getColor(R.color.gray_g06))
    }
}