package com.finpo.app.ui.filter

import android.widget.CheckBox
import androidx.databinding.BindingAdapter

@BindingAdapter("checkboxId", "InterestIds")
fun setChecked(
    checkBox: CheckBox,
    checkboxId: Int,
    InterestIds: IntArray
) {
    checkBox.isChecked = checkboxId in InterestIds
}