package com.finpo.app.ui.alarm

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R
import com.finpo.app.model.remote.NotificationHistoryContent
import com.finpo.app.utils.FCM_TYPE.POLICY
import com.finpo.app.utils.TimeFormatter
import com.skydoves.balloon.textForm

@BindingAdapter("notiDate")
fun setNotiDate(textView: TextView, data: NotificationHistoryContent?) {
    if(data == null) return
    val date = data.createdAt ?: (data.policy?.modifiedAt ?: "")
    textView.text=TimeFormatter.formatTime(date)
}

@BindingAdapter("notiTitle")
fun setNotiTitle(textView: TextView, data: NotificationHistoryContent?) {
    if(data == null) return
    textView.text = data.policy?.title ?: data.post?.content ?: ""
}

@BindingAdapter("notiAdditionalTitle")
fun setAdditionalNotiTitle(textView: TextView, data: NotificationHistoryContent?) {
    if(data == null) return
    textView.text =
        if(data.policy == null)
            textView.context.getString(R.string.noti_additional_comment_title)
        else
            textView.context.getString(R.string.noti_additional_policy_title)
}

@BindingAdapter("notiIcon")
fun setNotiIcon(imageView: ImageView, text: String) {
    if(text == POLICY) imageView.setImageResource(R.drawable.ic_noti_alarm)
    else imageView.setImageResource(R.drawable.ic_comment_alarm)
}