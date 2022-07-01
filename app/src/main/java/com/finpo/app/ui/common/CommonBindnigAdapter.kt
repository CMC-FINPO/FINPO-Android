package com.finpo.app.ui.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("isBookmarkChecked")
fun setBookmarkImg(
    imageView: ImageView,
    isBookmarkChecked: Boolean
) {
    if(isBookmarkChecked) imageView.setBackgroundResource(R.drawable.ic_scrap_active)
    else imageView.setBackgroundResource(R.drawable.ic_scrap_inactive)
}