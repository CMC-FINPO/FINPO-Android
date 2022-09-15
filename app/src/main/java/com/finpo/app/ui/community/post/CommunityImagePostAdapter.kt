package com.finpo.app.ui.community.post

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerCommunityPostImgBinding

class CommunityImagePostAdapter(val viewModel: CommunityPostViewModel) :
    ListAdapter<Uri, CommunityImagePostAdapter.ImageHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageHolder {
        val binding = ItemRecyclerCommunityPostImgBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(currentList[position])
    }

    inner class ImageHolder(private val binding: ItemRecyclerCommunityPostImgBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Uri) {
            binding.uri = data
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Uri,
                newItem: Uri
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}