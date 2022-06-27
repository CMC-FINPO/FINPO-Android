package com.finpo.app.ui.filter

import android.annotation.SuppressLint
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class FilterViewHolder(private val binding: ItemRecyclerFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CategoryChildFormat) {
            binding.data = data

            if(binding.rvCategoryChildren.adapter == null) initRecyclerView(data)
            else notifyDataSetChange()
        }

        private fun initRecyclerView(data: CategoryChildFormat) {
            val filterDetailAdapter = FilterDetailAdapter(viewModel)
            filterDetailAdapter.setHasStableIds(true)
            binding.rvCategoryChildren.layoutManager = FlexboxLayoutManager(binding.root.context)
            binding.rvCategoryChildren.adapter = filterDetailAdapter
            binding.rvCategoryChildren.itemAnimator = null
            filterDetailAdapter.submitList(data.childs)
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun notifyDataSetChange() {
            binding.rvCategoryChildren.adapter?.notifyDataSetChanged()
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