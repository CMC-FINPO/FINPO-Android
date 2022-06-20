package com.finpo.app.ui.my_page

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.finpo.app.R

@BindingAdapter("circleImageUrl", "gender")
fun loadCircleImage(view: ImageView, imageUrl: String?, gender: String?) {
    val defaultImage = if(gender == view.context.getString(R.string.male_eng)) R.drawable.ic_profile_male_60
    else R.drawable.ic_profile_female_60
    view.setImageResource(defaultImage)
    if(!imageUrl.isNullOrEmpty()) {
        Glide.with(view)
            .load(imageUrl)
            .placeholder(defaultImage)
            .circleCrop()
            .into(view)
    }
}