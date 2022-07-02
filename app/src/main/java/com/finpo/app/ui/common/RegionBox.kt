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
import com.finpo.app.databinding.CustomRegionBoxBinding

class RegionBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: CustomRegionBoxBinding

    var regionText: String? = ""
        set(value) {
            field = value
            if(value == null) return
            binding.tvRegion.text = value
        }

    var regionDelete: (id: Int) -> Unit = {}
        set(value) {
            field = value
            binding.ivRegionRemove.setOnClickListener { value(id) }
        }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_region_box, this, true)
    }
}