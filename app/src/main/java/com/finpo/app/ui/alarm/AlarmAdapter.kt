package com.finpo.app.ui.alarm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerNotiHistoryBinding
import com.finpo.app.databinding.ItemRecyclerPolicyBinding
import com.finpo.app.databinding.ItemRecyclerPolicyLoadingBinding
import com.finpo.app.model.remote.NotificationHistoryContent
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.utils.PolicyRecyclerViewType.LOADING
import com.finpo.app.utils.PolicyRecyclerViewType.CONTENT
import java.lang.Exception

class AlarmAdapter(val viewModel: AlarmViewModel)
    : ListAdapter<NotificationHistoryContent, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return if(currentList[position] == null) LOADING
        else CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            CONTENT -> {
                val binding = ItemRecyclerNotiHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PolicyHolder(binding)
            }
            else -> {
                val binding = ItemRecyclerPolicyLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currentList[position]?.let { (holder as PolicyHolder).setData(it) }
    }

    inner class LoadingHolder(private val binding: ItemRecyclerPolicyLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PolicyHolder(private val binding: ItemRecyclerNotiHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: NotificationHistoryContent) {
            binding.data = data
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<NotificationHistoryContent>() {
            override fun areItemsTheSame(oldItem: NotificationHistoryContent, newItem: NotificationHistoryContent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationHistoryContent,
                newItem: NotificationHistoryContent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}