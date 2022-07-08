package com.finpo.app.ui.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerInterestPolicyBinding
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.ui.common.PolicyContentDiffUtil

class InterestPolicyAdapter(val viewModel: BookmarkViewModel)
    : ListAdapter<PolicyContent, InterestPolicyAdapter.PolicyHolder>(PolicyContentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyHolder {
        val binding = ItemRecyclerInterestPolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PolicyHolder(binding)
    }

    override fun onBindViewHolder(holder: PolicyHolder, position: Int) {
        currentList[position]?.let { holder.setData(it) }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class PolicyHolder(private val binding: ItemRecyclerInterestPolicyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: PolicyContent) {
            binding.data = data
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}