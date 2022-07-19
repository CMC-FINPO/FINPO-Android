package com.finpo.app.ui.common

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.finpo.app.R
import com.finpo.app.model.remote.*
import com.finpo.app.utils.SORT_TYPE
import com.finpo.app.utils.TimeFormatter
import com.skydoves.balloon.textForm

@BindingAdapter("circleImageUrl", "gender")
fun loadCircleImage(view: ImageView, imageUrl: String?, gender: String?) {
    val defaultImage = if(gender == view.context.getString(R.string.male_eng)) R.drawable.ic_profile_male_60
    else R.drawable.ic_profile_female_60
    if(!imageUrl.isNullOrEmpty()) {
        Glide.with(view)
            .load(imageUrl)
            .placeholder(defaultImage)
            .circleCrop()
            .into(view)
    } else view.setImageResource(defaultImage)
}

@BindingAdapter("isBookmarkCheckedLarge")
fun setBookmarkImgLarge(
    imageView: ImageView,
    isBookmarkChecked: Boolean
) {
    if(isBookmarkChecked) imageView.setBackgroundResource(R.drawable.ic_scrap_active)
    else imageView.setBackgroundResource(R.drawable.ic_scrap_inactive)
}

@BindingAdapter("isBookmarkCheckedSmall")
fun setBookmarkImgSmall(
    imageView: ImageView,
    isBookmarkChecked: Boolean
) {
    if(isBookmarkChecked) imageView.setBackgroundResource(R.drawable.ic_scrap_active_small)
    else imageView.setBackgroundResource(R.drawable.ic_scrap_inactive_small)
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

@BindingAdapter("commentContentDate")
fun setCommentDate(textView: TextView, data: CommentContent?) {
    if(data == null) return
    val date = if(data.isModified == true) data.modifiedAt else data.createdAt
    textView.text= TimeFormatter.formatTime(date ?: "")
}

@BindingAdapter("commentReplyContentDate")
fun setCommentReplyDate(textView: TextView, data: CommentChilds?) {
    if(data == null) return
    val date = if(data.isModified == true) data.modifiedAt else data.createdAt
    textView.text= TimeFormatter.formatTime(date ?: "")
}

@BindingAdapter("communityNickname")
fun setCommunityNickname(textView: TextView, data: WritingContent?) {
    if(data == null) return
    if(data.isUserWithdraw) {
        textView.text = textView.context.getString(R.string.unknownUser)
        textView.setTextColor(textView.context.getColor(R.color.gray_g03))
    } else {
        textView.text = data.user?.nickname ?: ""
        textView.setTextColor(textView.context.getColor(R.color.black_b01))
    }
}

@BindingAdapter("commentNickname")
fun setCommentNickname(textView: TextView, data: CommentContent?) {
    if(data == null) return
    if(data.isUserWithdraw == true) {
        textView.text = textView.context.getString(R.string.unknownUser)
        textView.setTextColor(textView.context.getColor(R.color.gray_g03))
    } else {
        textView.text = data.user?.nickname ?: "익명"
        textView.setTextColor(textView.context.getColor(R.color.black_b01))
    }
}

@BindingAdapter("commentReplyNickname")
fun setCommentReplyNickname(textView: TextView, data: CommentChilds?) {
    if(data == null) return
    if(data.isUserWithdraw == true) {
        textView.text = textView.context.getString(R.string.unknownUser)
        textView.setTextColor(textView.context.getColor(R.color.gray_g03))
    } else {
        textView.text = data.user?.nickname ?: "익명"
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

@BindingAdapter("spinnerText")
fun setSpinnerText(
    textView: TextView,
    sortType: Int
) {
    textView.text = if(sortType == SORT_TYPE.RECENT)    textView.context.getString(R.string.recent)
    else textView.context.getString(R.string.popular)
}

@BindingAdapter("userSortType", "viewSortType")
fun setFont(
    textView: TextView,
    viewSortType: Int,
    userSortType: Int
) {
    if(viewSortType == userSortType) {
        textView.typeface = ResourcesCompat.getFont(textView.context, R.font.notosans_medium)
        textView.setTextColor(textView.context.getColor(R.color.point_p01))
    }
    else {
        textView.typeface = ResourcesCompat.getFont(textView.context, R.font.notosans_regular)
        textView.setTextColor(textView.context.getColor(R.color.black_b01))
    }
}

@BindingAdapter("isRecyclerViewItemVisible", "isBlocked")
fun checkWritingDelete(view: View, isRecyclerViewItemVisible: Boolean, isBlocked: Boolean?) {
    //XML상에서 GONE 설정 시 여백이 제거 안됨
    val params: ViewGroup.LayoutParams = view.layoutParams
    if(!isRecyclerViewItemVisible || isBlocked == true) {
        view.visibility = View.GONE
        params.width = 0
        params.height = 0
    } else {
        view.visibility = View.VISIBLE
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }
    view.layoutParams = params
}

@BindingAdapter("commentData")
fun checkCommentDelete(view: View, commentData: CommentContent) {
    val params: ViewGroup.LayoutParams = view.layoutParams
    var hasChildren = false
    commentData.childs?.forEach {
        if(it.status && (it.isBlocked == false || it.isBlocked == null)) {
            hasChildren = true
            return@forEach
        }
    }
    Log.d("replyDelete","hasChildren = ${hasChildren}")
    if(!hasChildren || commentData.status || (commentData.isUserBlocked == false)) {
        view.visibility = View.GONE
        params.width = 0
        params.height = 0
    } else {
        view.visibility = View.VISIBLE
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }
    view.layoutParams = params
}

