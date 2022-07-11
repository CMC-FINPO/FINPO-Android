package com.finpo.app.ui.common

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.finpo.app.R
import com.finpo.app.model.remote.NotificationHistoryContent
import com.finpo.app.model.remote.RegionInterest
import com.finpo.app.model.remote.WritingContent
import com.finpo.app.utils.TimeFormatter
import com.skydoves.balloon.textForm

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

@BindingAdapter("isBookmarkChecked")
fun setBookmarkImg(
    imageView: ImageView,
    isBookmarkChecked: Boolean
) {
    if(isBookmarkChecked) imageView.setBackgroundResource(R.drawable.ic_scrap_active)
    else imageView.setBackgroundResource(R.drawable.ic_scrap_inactive)
}

@BindingAdapter("isLiked")
fun setLikeImg(
    imageView: ImageView,
    isLiked: Boolean
) {
    if(isLiked) imageView.setBackgroundResource(R.drawable.ic_like_active)
    else imageView.setBackgroundResource(R.drawable.ic_like_inactive)
}

@BindingAdapter("writingContentDate")
fun setWritingDate(textView: TextView, data: WritingContent?) {
    if(data == null) return
    val date = if(data.isModified == true) data.modifiedAt else data.createdAt
    textView.text= TimeFormatter.formatTime(date)
}

@BindingAdapter("communityNickname")
fun setCommunityNickname(textView: TextView, data: WritingContent?) {
    if(data == null) return
    if(data.isUserWithdraw) {
        textView.text = textView.context.getString(R.string.unknownUser)
        textView.setTextColor(textView.context.getColor(R.color.gray_g05)) // TODO 색상 변경 필요
    } else {
        textView.text = data.user?.nickname ?: ""
        textView.setTextColor(textView.context.getColor(R.color.black_b01))
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if(!imageUrl.isNullOrEmpty()) {
        Glide.with(view)
            .load(imageUrl)
            .into(view)
    }
}

@BindingAdapter("regionData")
fun setRegionText(textView: TextView, regionData: RegionInterest?) {
    if(regionData == null)  return
    val (detailRegion, region) =
        if(regionData.parent == null) arrayOf("전체", regionData.name ?: "")
        else arrayOf(regionData.name, regionData.parent.name)

    textView.text = textView.context.getString(R.string.region_text, region, detailRegion)
}

