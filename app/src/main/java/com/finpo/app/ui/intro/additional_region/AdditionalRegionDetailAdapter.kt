package com.finpo.app.ui.intro.additional_region

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerAdditionalRegionDetailBinding
import com.finpo.app.databinding.ItemRecyclerRegionBinding
import com.finpo.app.databinding.ItemRecyclerRegionDetailBinding
import com.finpo.app.model.remote.Region
import com.finpo.app.ui.intro.living_area.RegionDiffUtil
import javax.inject.Inject

class AdditionalRegionDetailAdapter @Inject constructor(val viewModel: AdditionalRegionLiveData) :
    ListAdapter<Region, AdditionalRegionDetailAdapter.RegionViewHolder>(RegionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(
            ItemRecyclerAdditionalRegionDetailBinding.inflate(
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

    inner class RegionViewHolder(private val binding: ItemRecyclerAdditionalRegionDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Region) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

}