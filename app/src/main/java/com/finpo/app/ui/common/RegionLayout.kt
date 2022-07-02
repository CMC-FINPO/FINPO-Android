package com.finpo.app.ui.common

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.indices
import androidx.databinding.DataBindingUtil
import com.finpo.app.R
import com.finpo.app.databinding.CustomRegionLayoutBinding
import com.google.android.flexbox.FlexboxLayout

class RegionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FlexboxLayout(context, attrs, defStyleAttr) {
    private val binding: CustomRegionLayoutBinding
    private val regionTextViewList: List<TextView>
    private val regionContainerList: List<ConstraintLayout>
    private val regionDeleteList: List<ImageView>
    var regionCount: Int? = 0
    set(value) {
        field = value
        if(value == null) return
        binding.clRegionNone.visibility = if(value == 0) View.VISIBLE else View.GONE
    }

    var regionTextList: MutableList<String>? = mutableListOf()
    set(value) {
        field = value
        if(value == null) return
        for (idx in 0 until value.size) {
            regionTextViewList[idx].text = value[idx]
            regionContainerList[idx].visibility = if(value[idx].isEmpty()) View.GONE else View.VISIBLE
        }
    }

    var regionDelete: (id: Int) -> Unit = {}
    set(value) {
        field = value
        for(idx in regionDeleteList.indices) {
            regionDeleteList[idx].setOnClickListener { value(idx) }
        }
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_region_layout, this, true)

        with(binding) {
            regionTextViewList = listOf(tvRegion1, tvRegion2, tvRegion3, tvRegion4, tvRegion5)
            regionContainerList = listOf(clRegion1, clRegion2, clRegion3, clRegion4, clRegion5)
            regionDeleteList = listOf(ivRegionRemove1, ivRegionRemove2, ivRegionRemove3, ivRegionRemove4, ivRegionRemove5)
        }
    }



}