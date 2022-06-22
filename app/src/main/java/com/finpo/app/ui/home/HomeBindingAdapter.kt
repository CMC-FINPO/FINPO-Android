package com.finpo.app.ui.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.finpo.app.R
import com.finpo.app.model.remote.RegionInterest

@BindingAdapter("regionData")
fun setRegionText(textView: TextView, regionData: RegionInterest) {
    val (detailRegion, region) =
        if(regionData.parent == null) arrayOf("전체", regionData.name ?: "")
     else arrayOf(regionData.name, regionData.parent.name)

    textView.text = textView.context.getString(R.string.region_text, region, detailRegion)
}