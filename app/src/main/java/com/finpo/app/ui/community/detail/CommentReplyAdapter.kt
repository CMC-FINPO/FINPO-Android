package com.finpo.app.ui.community.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.*
import com.finpo.app.model.remote.*
import com.finpo.app.utils.PopupWindowUtil
import javax.inject.Inject

class CommentReplyAdapter (val viewModel: CommunityDetailViewModel) :
    ListAdapter<CommentChilds, CommentReplyAdapter.CommentReplyViewHolder>(diffUtil) {

    var commentReplyPopup: PopupWindow? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentReplyViewHolder {
        return CommentReplyViewHolder(
            ItemRecyclerCommunityDetailCommentReplyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentReplyViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class CommentReplyViewHolder(private val binding: ItemRecyclerCommunityDetailCommentReplyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CommentChilds) {
            binding.data = data
            binding.viewModel = viewModel

            if(data.status)
                binding.ivMore.setOnClickListener {
                    commentReplyPopup = PopupWindowUtil(binding.root.context).commentReplyPopupWindow(viewModel, data, it)
                }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CommentChilds>() {
            override fun areContentsTheSame(oldItem: CommentChilds, newItem: CommentChilds) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: CommentChilds, newItem: CommentChilds) =
                oldItem.id == newItem.id
        }
    }

}