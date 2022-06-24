package com.finpo.app.ui.intro.interest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerInterestParentBinding
import com.finpo.app.databinding.ItemRecyclerRegionBinding
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.model.remote.Region
import javax.inject.Inject

class InterestAdapter @Inject constructor(val viewModel: InterestLiveData) :
    ListAdapter<ParentCategory, InterestAdapter.ParentCategoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentCategoryViewHolder {
        return ParentCategoryViewHolder(
            ItemRecyclerInterestParentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ParentCategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ParentCategoryViewHolder(private val binding: ItemRecyclerInterestParentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ParentCategory) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ParentCategory>() {
            override fun areContentsTheSame(oldItem: ParentCategory, newItem: ParentCategory) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ParentCategory, newItem: ParentCategory) =
                oldItem.id == newItem.id
        }
    }

}