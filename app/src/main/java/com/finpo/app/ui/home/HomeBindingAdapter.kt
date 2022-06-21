package com.finpo.app.ui.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R

@BindingAdapter("detailRegion", "region")
fun setRegionText(textView: TextView, detailRegion: String, region: String) {
    textView.text = textView.context.getString(R.string.region_text, region, detailRegion)
}