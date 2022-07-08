package com.finpo.app.ui.edit_region.edit_region_interest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerEditInterestRegionDetailBinding
import com.finpo.app.model.remote.Region
import com.finpo.app.ui.edit_region.EditRegionViewModel
import com.finpo.app.ui.common.RegionDiffUtil

class EditInterestRegionDetailAdapter(val viewModel: EditRegionViewModel) :
    ListAdapter<Region, EditInterestRegionDetailAdapter.RegionViewHolder>(RegionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(
            ItemRecyclerEditInterestRegionDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class RegionViewHolder(private val binding: ItemRecyclerEditInterestRegionDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Region) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

}