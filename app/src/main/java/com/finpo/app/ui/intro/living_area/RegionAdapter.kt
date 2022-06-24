package com.finpo.app.ui.intro.living_area

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerRegionBinding
import com.finpo.app.model.remote.Region
import javax.inject.Inject

class RegionAdapter @Inject constructor(val viewModel: LivingAreaLiveData) :
    ListAdapter<Region, RegionAdapter.RegionViewHolder>(RegionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(
            ItemRecyclerRegionBinding.inflate(
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

    inner class RegionViewHolder(private val binding: ItemRecyclerRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Region) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

}