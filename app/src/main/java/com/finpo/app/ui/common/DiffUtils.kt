package com.finpo.app.ui.common

import androidx.recyclerview.widget.DiffUtil
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.model.remote.Region

class RegionDiffUtil : DiffUtil.ItemCallback<Region>() {
    override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem == newItem
    }
}

class PolicyContentDiffUtil : DiffUtil.ItemCallback<PolicyContent>() {
    override fun areItemsTheSame(oldItem: PolicyContent, newItem: PolicyContent): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PolicyContent, newItem: PolicyContent): Boolean {
        return oldItem == newItem
    }
}