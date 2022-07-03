package com.finpo.app.ui.my_page

import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.size
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.finpo.app.R
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.utils.dp
import com.google.android.flexbox.FlexboxLayout

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

@BindingAdapter("interestList")
fun setInterestList(flexBox: FlexboxLayout, items: List<ParentCategory>?) {
    if (items == null || flexBox.size != 0)
        return
    for (item in items) {
        val textView = TextView(flexBox.context)
        textView.text = item.name

        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            22.dp
        )
        lp.setMargins(0, 0, 5.dp, 5.dp)
        lp.gravity = Gravity.TOP
        textView.layoutParams = lp

        textView.setTextAppearance(R.style.label2)
        textView.setTextColor(ContextCompat.getColor(flexBox.context, R.color.point_p01))
        textView.setBackgroundResource(R.drawable.bg_solid_g08_rounded_5)
        textView.setPadding(4.dp, 2.dp, 4.dp, 2.dp)
        textView.includeFontPadding = false

        flexBox.addView(textView)
    }
}