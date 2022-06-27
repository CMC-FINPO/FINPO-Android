package com.finpo.app.ui.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerFilterBinding
import com.finpo.app.databinding.ItemRecyclerFilterDetailBinding
import com.finpo.app.databinding.ItemRecyclerInterestParentBinding
import com.finpo.app.databinding.ItemRecyclerRegionBinding
import com.finpo.app.model.remote.CategoryChild
import com.finpo.app.model.remote.CategoryChildFormat
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.model.remote.Region
import javax.inject.Inject

class FilterDetailAdapter (val viewModel: FilterViewModel) :
    ListAdapter<CategoryChild, FilterDetailAdapter.FilterDetailViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterDetailViewHolder {
        return FilterDetailViewHolder(
            ItemRecyclerFilterDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FilterDetailViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class FilterDetailViewHolder(private val binding: ItemRecyclerFilterDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CategoryChild) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CategoryChild>() {
            override fun areContentsTheSame(oldItem: CategoryChild, newItem: CategoryChild) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: CategoryChild, newItem: CategoryChild) =
                oldItem.id == newItem.id
        }
    }

}