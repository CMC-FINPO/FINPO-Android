package com.finpo.app.ui.participation_list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("memoEditText")
fun safeNullText(textView: TextView, memoEditText: String?) {
    textView.text = if(memoEditText == null) textView.context.getString(R.string.write_memo_title)
    else textView.context.getString(R.string.edit_memo_title)
}