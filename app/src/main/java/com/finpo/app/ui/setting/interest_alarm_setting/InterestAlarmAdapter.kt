package com.finpo.app.ui.setting.interest_alarm_setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerInterestAlarmBinding
import com.finpo.app.databinding.ItemRecyclerInterestParentBinding
import com.finpo.app.databinding.ItemRecyclerParentCategoryBinding
import com.finpo.app.databinding.ItemRecyclerRegionBinding
import com.finpo.app.model.remote.InterestCategory
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.model.remote.Region
import javax.inject.Inject

class InterestAlarmAdapter(private val viewModel: InterestAlarmSettingViewModel) :
    ListAdapter<InterestCategory, InterestAlarmAdapter.ParentCategoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentCategoryViewHolder {
        return ParentCategoryViewHolder(
            ItemRecyclerInterestAlarmBinding.inflate(
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

    inner class ParentCategoryViewHolder(private val binding: ItemRecyclerInterestAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: InterestCategory) {
            binding.viewModel = viewModel
            binding.data = data
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<InterestCategory>() {
            override fun areContentsTheSame(oldItem: InterestCategory, newItem: InterestCategory) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: InterestCategory, newItem: InterestCategory) =
                oldItem.id == newItem.id
        }
    }

}