package com.finpo.app.ui.interest_setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.*
import com.finpo.app.model.remote.CategoryChild
import com.finpo.app.model.remote.CategoryChildFormat
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.model.remote.Region
import javax.inject.Inject

class InterestEditDetailAdapter (val viewModel: InterestSettingViewModel) :
    ListAdapter<CategoryChild, InterestEditDetailAdapter.FilterDetailViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterDetailViewHolder {
        return FilterDetailViewHolder(
            ItemRecyclerEditInterestDetailBinding.inflate(
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

    inner class FilterDetailViewHolder(private val binding: ItemRecyclerEditInterestDetailBinding) :
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