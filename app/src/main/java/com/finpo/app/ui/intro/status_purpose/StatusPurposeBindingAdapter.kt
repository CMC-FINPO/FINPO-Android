package com.finpo.app.ui.intro.status_purpose

import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("statusPurposeHeaderText")
fun setStatusPurposeHeaderText(textView: TextView, nickname: String) {
    textView.text = String.format(textView.context.getString(R.string.status_purpose_text), nickname)
}

@BindingAdapter("statusSelectedId", "statusId")
fun setStatusSelected(radioButton: RadioButton, statusSelectedId : Int, statusId: Int) {
    radioButton.isChecked = (statusSelectedId == statusId)
}