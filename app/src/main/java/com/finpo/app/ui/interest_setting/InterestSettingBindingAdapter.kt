package com.finpo.app.ui.interest_setting

import android.util.Log
import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.finpo.app.model.remote.CategoryRequest

@BindingAdapter("checkboxId", "InterestIds")
fun setChecked(
    checkBox: CheckBox,
    checkboxId: Int,
    InterestIds: List<CategoryRequest>?
) {
    if(InterestIds == null) return
    checkBox.isChecked = CategoryRequest(checkboxId) in InterestIds
}

@BindingAdapter("checkboxId", "purposeIds")
fun setChecked(
    checkBox: CheckBox,
    checkboxId: Int,
    purposeIds: Set<Int>
) {
    Log.d("purposeId","${purposeIds}")
    checkBox.isChecked = checkboxId in purposeIds
}