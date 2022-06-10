package com.finpo.app.ui.intro.default_info

import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.finpo.app.R
import com.finpo.app.utils.PAGE.ADDITIONAL_REGION
import com.finpo.app.utils.PAGE.AGREE
import com.finpo.app.utils.PAGE.DEFAULT_INFO
import com.finpo.app.utils.PAGE.INTEREST
import com.finpo.app.utils.PAGE.LOGIN
import com.finpo.app.utils.PAGE.REGION
import com.finpo.app.utils.PAGE.REGISTRATION
import com.finpo.app.utils.PAGE.STATE_PURPOSE

@BindingAdapter("nameErrorMsg")
fun setErrorMsg(
    textView: TextView,
    stringId: Int?
) {
    if (stringId != null) {
        textView.visibility = View.VISIBLE
        textView.text = textView.context.getString(stringId)
    } else
        textView.visibility = View.GONE
}

@BindingAdapter("setErrorColor")
fun setErrorColor(
    textView: TextView,
    isError: Boolean
) {
    if (isError)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.error_e01))
    else
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.gray_g01))
}

@BindingAdapter("setErrorColor")
fun setErrorColor(
    editText: EditText,
    isError: Boolean
) {
    if (isError)
        editText.setBackgroundResource(R.drawable.ll_edittext_underline_error)
    else
        editText.setBackgroundResource(R.drawable.selector_edittext)
}

@BindingAdapter("isNicknameError")
fun setErrorImageVisibility(
    imageView: ImageView,
    isNicknameError: Boolean
) {
    if(isNicknameError) imageView.visibility = View.VISIBLE
    else imageView.visibility = View.GONE
}

@BindingAdapter("isNicknameOverlap", "isNicknameBlank")
fun setOkImageVisibility(
    imageView: ImageView,
    isNicknameOverlap: Boolean,
    isNicknameBlank: String?
) {
    if(!isNicknameOverlap && !isNicknameBlank.isNullOrBlank()) imageView.visibility = View.VISIBLE
    else imageView.visibility = View.GONE
}

@BindingAdapter("changeFont")
fun setFont(
    radioButton: RadioButton,
    isChecked: Boolean
) {
    radioButton.typeface = if(isChecked) ResourcesCompat.getFont(radioButton.context, R.font.notosans_medium)
    else ResourcesCompat.getFont(radioButton.context, R.font.notosans_regular)
}