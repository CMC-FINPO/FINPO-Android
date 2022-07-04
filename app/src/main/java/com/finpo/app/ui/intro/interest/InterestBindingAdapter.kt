package com.finpo.app.ui.intro.interest

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.finpo.app.R

@BindingAdapter("interestText")
fun setInterestText(textView: TextView, nickname: String) {
    textView.text = String.format(textView.context.getString(R.string.interest_text), nickname)
}

@BindingAdapter("isInterestChecked")
fun checkInterestChecked(constraintLayout: ConstraintLayout, isInterestChecked: Boolean) {
    if(isInterestChecked)
        constraintLayout.setBackgroundResource(R.drawable.bg_solid_p03_rounded_p01_5)
    else
        constraintLayout.setBackgroundResource(R.drawable.bg_solid_none_rounded_g06_5)
}

@BindingAdapter("isInterestChecked")
fun checkInterestChecked(textView: TextView, isInterestChecked: Boolean) {
    if(isInterestChecked)
        textView.setTextColor(textView.context.getColor(R.color.point_p01))
    else
        textView.setTextColor(textView.context.getColor(R.color.black_b01))
}