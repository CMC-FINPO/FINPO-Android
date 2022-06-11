package com.finpo.app.ui.intro.interest

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("interestText")
fun setInterestText(textView: TextView, nickname: String) {
    textView.text = String.format(textView.context.getString(R.string.interest_text), nickname)
}