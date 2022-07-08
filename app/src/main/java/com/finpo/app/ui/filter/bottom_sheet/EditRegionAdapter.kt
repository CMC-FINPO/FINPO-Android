package com.finpo.app.ui.filter.bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerEditRegionBinding
import com.finpo.app.model.remote.Region
import com.finpo.app.ui.filter.FilterViewModel
import com.finpo.app.ui.common.RegionDiffUtil

class EditRegionAdapter (val viewModel: FilterViewModel) :
    ListAdapter<Region, EditRegionAdapter.RegionViewHolder>(RegionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(
            ItemRecyclerEditRegionBinding.inflate(
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

    inner class RegionViewHolder(private val binding: ItemRecyclerEditRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Region) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

}