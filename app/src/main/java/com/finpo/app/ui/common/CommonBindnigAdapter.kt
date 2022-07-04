package com.finpo.app.ui.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.finpo.app.R

@BindingAdapter("isBookmarkChecked")
fun setBookmarkImg(
    imageView: ImageView,
    isBookmarkChecked: Boolean
) {
    if(isBookmarkChecked) imageView.setBackgroundResource(R.drawable.ic_scrap_active)
    else imageView.setBackgroundResource(R.drawable.ic_scrap_inactive)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if(!imageUrl.isNullOrEmpty()) {
        Glide.with(view)
            .load(imageUrl)
            .into(view)
    }
}