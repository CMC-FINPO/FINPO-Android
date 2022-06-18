package com.finpo.app.ui.my_page

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.finpo.app.R

@BindingAdapter("circleImageUrl", "gender")
fun loadCircleImage(view: ImageView, imageUrl: String?, gender: String?) {
    if(gender == view.context.getString(R.string.male_eng))
        view.setImageResource(R.drawable.ic_profile_male_60)
    else
        view.setImageResource(R.drawable.ic_profile_female_60)
    if(!imageUrl.isNullOrEmpty()) {
        Glide.with(view)
            .load(imageUrl)
            .circleCrop()
            .into(view)
    }
}