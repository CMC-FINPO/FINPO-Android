package com.finpo.app.ui.community.detail.bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerReportBinding
import com.finpo.app.model.remote.IdReason
import com.finpo.app.ui.community.detail.CommunityDetailViewModel

class ReportAdapter (val viewModel: CommunityDetailViewModel) :
    ListAdapter<IdReason, ReportAdapter.ReportViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        return ReportViewHolder(
            ItemRecyclerReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ReportViewHolder(private val binding: ItemRecyclerReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: IdReason) {
            binding.data = data
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<IdReason>() {
            override fun areContentsTheSame(oldItem: IdReason, newItem: IdReason) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: IdReason, newItem: IdReason) =
                oldItem.id == newItem.id
        }
    }

}