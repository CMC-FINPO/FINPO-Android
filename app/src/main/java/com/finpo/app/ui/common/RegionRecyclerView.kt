package com.finpo.app.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.R
import com.finpo.app.databinding.CustomRegionRecyclerviewBinding

class RegionRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: CustomRegionRecyclerviewBinding
    val rvRegionAll: RecyclerView
    val rvRegionDetail: RecyclerView

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_region_recyclerview, this, true)

        rvRegionAll = binding.rvRegionAll
        rvRegionDetail = binding.rvRegionDetail
    }
}