package com.finpo.app.ui.bookmark

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerInterestPolicyBinding
import com.finpo.app.databinding.ItemRecyclerPolicyBinding
import com.finpo.app.databinding.ItemRecyclerPolicyLoadingBinding
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.utils.PolicyRecyclerViewType.LOADING
import com.finpo.app.utils.PolicyRecyclerViewType.CONTENT
import java.lang.Exception

class InterestPolicyAdapter(val viewModel: BookmarkViewModel)
    : ListAdapter<PolicyContent, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecyclerInterestPolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PolicyHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else (holder as PolicyHolder).setData(payloads[0] as PolicyContent, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currentList[position]?.let { (holder as PolicyHolder).setData(it, position) }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class PolicyHolder(private val binding: ItemRecyclerInterestPolicyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: PolicyContent, position: Int) {
            binding.data = data
            binding.viewModel = viewModel
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