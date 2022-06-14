package com.finpo.app.ui.intro.living_area

import android.util.Log
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("livingAreaText")
fun setLivingAreaText(textView: TextView, nickname: String) {
    textView.text = String.format(textView.context.getString(R.string.living_area_text), nickname)
}

@BindingAdapter("regionSelectedId", "regionId")
fun setRegionSelected(radioButton: RadioButton, regionSelectedId : Int, regionId: Int) {
    radioButton.isChecked = (regionSelectedId == regionId)
}