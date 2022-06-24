package com.finpo.app.ui.intro.additional_region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerAdditionalRegionBinding
import com.finpo.app.databinding.ItemRecyclerRegionBinding
import com.finpo.app.model.remote.Region
import com.finpo.app.ui.intro.living_area.RegionDiffUtil
import javax.inject.Inject

class AdditionalRegionAdapter @Inject constructor(val viewModel: AdditionalRegionLiveData) :
    ListAdapter<Region, AdditionalRegionAdapter.RegionViewHolder>(RegionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(
            ItemRecyclerAdditionalRegionBinding.inflate(
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

    inner class RegionViewHolder(private val binding: ItemRecyclerAdditionalRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Region) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

}