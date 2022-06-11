package com.finpo.app.ui.intro.living_area

import androidx.recyclerview.widget.DiffUtil
import com.finpo.app.model.remote.Region

class RegionDiffUtil : DiffUtil.ItemCallback<Region>() {
    override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem == newItem
    }
}