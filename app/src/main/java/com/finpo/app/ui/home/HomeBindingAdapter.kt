package com.finpo.app.ui.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R
import com.finpo.app.model.remote.RegionInterest

@BindingAdapter("region")
fun setRegionText(textView: TextView, region: RegionInterest) {
    textView.text = textView.context.getString(R.string.region_text, region.name, region.parent?.name ?: "전체")
}