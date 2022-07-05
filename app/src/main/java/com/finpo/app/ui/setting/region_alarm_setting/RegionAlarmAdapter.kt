package com.finpo.app.ui.setting.region_alarm_setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.*
import com.finpo.app.model.remote.*
import javax.inject.Inject

class RegionAlarmAdapter(private val viewModel: RegionAlarmSettingViewModel) :
    ListAdapter<InterestRegion, RegionAlarmAdapter.ParentCategoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentCategoryViewHolder {
        return ParentCategoryViewHolder(
            ItemRecyclerRegionAlarmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ParentCategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ParentCategoryViewHolder(private val binding: ItemRecyclerRegionAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: InterestRegion) {
            binding.viewModel = viewModel
            binding.data = data
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<InterestRegion>() {
            override fun areContentsTheSame(oldItem: InterestRegion, newItem: InterestRegion) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: InterestRegion, newItem: InterestRegion) =
                oldItem.id == newItem.id
        }
    }

}