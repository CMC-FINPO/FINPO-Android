package com.finpo.app.ui.edit_region.edit_region_living

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerEditLivingRegionBinding
import com.finpo.app.model.remote.Region
import com.finpo.app.ui.edit_region.EditRegionViewModel
import com.finpo.app.ui.common.RegionDiffUtil

class EditLivingRegionAdapter (val viewModel: EditRegionViewModel) :
    ListAdapter<Region, EditLivingRegionAdapter.RegionViewHolder>(RegionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(
            ItemRecyclerEditLivingRegionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class RegionViewHolder(private val binding: ItemRecyclerEditLivingRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Region) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

}