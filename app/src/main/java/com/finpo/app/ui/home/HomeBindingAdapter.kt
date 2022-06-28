package com.finpo.app.ui.home

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.finpo.app.R
import com.finpo.app.model.remote.RegionInterest
import com.finpo.app.utils.SORT_TYPE

@BindingAdapter("regionData")
fun setRegionText(textView: TextView, regionData: RegionInterest) {
    val (detailRegion, region) =
        if(regionData.parent == null) arrayOf("전체", regionData.name ?: "")
     else arrayOf(regionData.name, regionData.parent.name)

    textView.text = textView.context.getString(R.string.region_text, region, detailRegion)
}

@BindingAdapter("userSortType", "viewSortType")
fun setFont(
    textView: TextView,
    viewSortType: Int,
    userSortType: Int
) {
    if(viewSortType == userSortType) {
        textView.typeface = ResourcesCompat.getFont(textView.context, R.font.notosans_medium)
        textView.setTextColor(textView.context.getColor(R.color.point_p01))
    }
    else {
         textView.typeface = ResourcesCompat.getFont(textView.context, R.font.notosans_regular)
        textView.setTextColor(textView.context.getColor(R.color.black_b01))
    }
}

@BindingAdapter("spinnerText")
fun setSpinnerText(
    textView: TextView,
    sortType: Int
) {
    textView.text = if(sortType == SORT_TYPE.RECENT)    textView.context.getString(R.string.recent)
    else textView.context.getString(R.string.popular)
}

@BindingAdapter("isBookmarkChecked")
fun setSpinnerText(
    imageView: ImageView,
    isBookmarkChecked: Boolean
) {
    if(isBookmarkChecked) imageView.setBackgroundResource(R.drawable.ic_scrap_active)
    else imageView.setBackgroundResource(R.drawable.ic_scrap_inactive)
}