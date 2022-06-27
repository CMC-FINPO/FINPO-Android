package com.finpo.app.ui.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerFilterBinding
import com.finpo.app.databinding.ItemRecyclerInterestParentBinding
import com.finpo.app.databinding.ItemRecyclerRegionBinding
import com.finpo.app.model.remote.CategoryChildFormat
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.model.remote.Region
import com.finpo.app.utils.GridSpacingItemDecoration
import com.finpo.app.utils.dp
import com.google.android.flexbox.FlexboxLayoutManager
import javax.inject.Inject

class FilterAdapter (val viewModel: FilterViewModel) :
    ListAdapter<CategoryChildFormat, FilterAdapter.FilterViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            ItemRecyclerFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class FilterViewHolder(private val binding: ItemRecyclerFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CategoryChildFormat) {
            binding.data = data

            val filterDetailAdapter = FilterDetailAdapter(viewModel)
            binding.rvCategoryChildren.layoutManager = FlexboxLayoutManager(binding.root.context)
            binding.rvCategoryChildren.addItemDecoration(GridSpacingItemDecoration(3, 12.dp, false))
            binding.rvCategoryChildren.adapter = filterDetailAdapter
            binding.rvCategoryChildren.itemAnimator = null
            filterDetailAdapter.submitList(data.childs)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CategoryChildFormat>() {
            override fun areContentsTheSame(oldItem: CategoryChildFormat, newItem: CategoryChildFormat) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: CategoryChildFormat, newItem: CategoryChildFormat) =
                oldItem.id == newItem.id
        }
    }

}