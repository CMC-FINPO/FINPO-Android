package com.finpo.app.ui.intro.interest

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.finpo.app.R

@BindingAdapter("interestText")
fun setInterestText(textView: TextView, nickname: String) {
    textView.text = String.format(textView.context.getString(R.string.interest_text), nickname)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if(!imageUrl.isNullOrEmpty()) {
        Glide.with(view)
            .load(imageUrl)
            .into(view)
    }
}