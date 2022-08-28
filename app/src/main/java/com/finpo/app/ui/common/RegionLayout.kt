package com.finpo.app.ui.common

import android.content.Context
import android.transition.TransitionManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.indices
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.finpo.app.R
import com.finpo.app.databinding.CustomRegionLayoutBinding
import com.finpo.app.utils.MAX_FILTER_REGION_COUNT
import com.google.android.flexbox.FlexboxLayout

class RegionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FlexboxLayout(context, attrs, defStyleAttr) {
    private val binding: CustomRegionLayoutBinding
    private val regionContainerList: List<RegionBox>
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
            regionContainerList[idx].regionText = value[idx]
            regionContainerList[idx].visibility = if(value[idx].isEmpty()) View.GONE else View.VISIBLE
        }

        for(idx in value.size until MAX_FILTER_REGION_COUNT)
            regionContainerList[idx].visibility = View.GONE
    }

    var regionDelete: (id: Int) -> Unit = {}
    set(value) {
        field = value
        for(idx in regionContainerList.indices) {
            regionContainerList[idx].regionDelete = { value(idx) }
        }
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_region_layout, this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RegionLayout, defStyleAttr, 0)
        val emptyText = typedArray.getString(R.styleable.RegionLayout_emptyText)

        with(binding) {
            tvRegionNone.text = emptyText
            regionContainerList = listOf(regionBox1, regionBox2, regionBox3, regionBox4, regionBox5, regionBox6)
        }

        typedArray.recycle()
    }
}