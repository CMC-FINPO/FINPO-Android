package com.finpo.app.ui.community_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerCommunityDetailCommentBinding
import com.finpo.app.databinding.ItemRecyclerPolicyBinding
import com.finpo.app.databinding.ItemRecyclerPolicyLoadingBinding
import com.finpo.app.model.remote.CommentContent
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.utils.PolicyRecyclerViewType.LOADING
import com.finpo.app.utils.PolicyRecyclerViewType.CONTENT
import java.lang.Exception

class CommentAdapter(val viewModel: CommunityDetailViewModel)
    : ListAdapter<CommentContent, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return if(currentList[position] == null) LOADING
        else CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            CONTENT -> {
                val binding = ItemRecyclerCommunityDetailCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CommentHolder(binding)
            }
            else -> {
                val binding = ItemRecyclerPolicyLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currentList[position]?.let { (holder as CommentHolder).setData(it) }
    }

    inner class LoadingHolder(private val binding: ItemRecyclerPolicyLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CommentHolder(private val binding: ItemRecyclerCommunityDetailCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CommentContent) {
            binding.data = data
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<CommentContent>() {
            override fun areItemsTheSame(oldItem: CommentContent, newItem: CommentContent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CommentContent,
                newItem: CommentContent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}