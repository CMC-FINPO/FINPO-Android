package com.finpo.app.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerPolicyBinding
import com.finpo.app.databinding.ItemRecyclerPolicyLoadingBinding
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.utils.PolicyRecyclerViewType.LOADING
import com.finpo.app.utils.PolicyRecyclerViewType.CONTENT
import java.lang.Exception

class PolicyAdapter(val viewModel: HomeViewModel)
    : ListAdapter<PolicyContent, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return if(currentList[position] == null) LOADING
        else CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            CONTENT -> {
                val binding = ItemRecyclerPolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PolicyHolder(binding)
            }
            else -> {
                val binding = ItemRecyclerPolicyLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else (holder as PolicyHolder).setData(payloads[0] as PolicyContent, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currentList[position]?.let { (holder as PolicyHolder).setData(it, position) }
    }

    inner class LoadingHolder(private val binding: ItemRecyclerPolicyLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PolicyHolder(private val binding: ItemRecyclerPolicyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: PolicyContent, position: Int) {
            binding.data = data
            binding.viewModel = viewModel
            binding.position = position
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PolicyContent>() {
            override fun areItemsTheSame(oldItem: PolicyContent, newItem: PolicyContent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PolicyContent,
                newItem: PolicyContent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}