package com.finpo.app.ui.community_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
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
import com.finpo.app.utils.PopupWindowUtil
import java.lang.Exception

class CommentAdapter(val viewModel: CommunityDetailViewModel)
    : ListAdapter<CommentContent, RecyclerView.ViewHolder>(diffUtil) {

    var commentPopup: PopupWindow? = null

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
            //XML상에서 GONE 설정 시 여백이 제거 안됨
            if(!data.status) {
                binding.root.visibility = View.GONE
                binding.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            } else {
                binding.root.visibility = View.VISIBLE
                binding.root.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            binding.data = data
            binding.viewModel = viewModel

            //TODO MVVM 적용 시킬 방법이 안떠오름 ...
            binding.ivMore.setOnClickListener {
                commentPopup = PopupWindowUtil(binding.root.context).commentPopupWindow(viewModel, data, it)
            }

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