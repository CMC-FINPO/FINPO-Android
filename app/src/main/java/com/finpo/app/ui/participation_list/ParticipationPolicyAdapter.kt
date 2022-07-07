package com.finpo.app.ui.participation_list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerInterestPolicyBinding
import com.finpo.app.databinding.ItemRecyclerParticipationPolicyBinding
import com.finpo.app.databinding.ItemRecyclerPolicyBinding
import com.finpo.app.databinding.ItemRecyclerPolicyLoadingBinding
import com.finpo.app.model.remote.ParticipationPolicy
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.utils.PolicyRecyclerViewType.LOADING
import com.finpo.app.utils.PolicyRecyclerViewType.CONTENT
import java.lang.Exception

class ParticipationPolicyAdapter(val viewModel: ParticipationListViewModel)
    : ListAdapter<ParticipationPolicy, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecyclerParticipationPolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PolicyHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else (holder as PolicyHolder).setData(payloads[0] as ParticipationPolicy, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currentList[position]?.let { (holder as PolicyHolder).setData(it, position) }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class PolicyHolder(private val binding: ItemRecyclerParticipationPolicyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: ParticipationPolicy, position: Int) {
            binding.data = data
            binding.position = position
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ParticipationPolicy>() {
            override fun areItemsTheSame(oldItem: ParticipationPolicy, newItem: ParticipationPolicy): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ParticipationPolicy,
                newItem: ParticipationPolicy
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}