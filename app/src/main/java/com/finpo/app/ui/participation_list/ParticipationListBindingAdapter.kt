package com.finpo.app.ui.participation_list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("memoEditText")
fun memoTitle(textView: TextView, memoEditText: String?) {
    textView.text = if(memoEditText == null) textView.context.getString(R.string.write_memo_title)
    else textView.context.getString(R.string.edit_memo_title)
}

@BindingAdapter("memoTitle")
fun memoTitle(textView: TextView, isEditMode: Boolean) {
    textView.text = if(!isEditMode) textView.context.getString(R.string.write_memo_title)
    else textView.context.getString(R.string.edit_memo_title)
}

@BindingAdapter("memoButtonText")
fun memoButton(textView: TextView, isEditMode: Boolean) {
    textView.text = if(!isEditMode) textView.context.getString(R.string.write)
    else textView.context.getString(R.string.edit)
}