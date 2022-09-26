package com.finpo.app.ui.community.detail.image_viewer

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerCommunityDetailImgBinding
import com.finpo.app.databinding.ItemRecyclerImgViewerBinding
import com.finpo.app.model.remote.ImageOrder
import javax.inject.Inject

class ImageViewerAdapter @Inject constructor() :
    ListAdapter<ImageOrder, ImageViewerAdapter.ImageHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageHolder {
        val binding = ItemRecyclerImgViewerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(currentList[position])
    }

    inner class ImageHolder(private val binding: ItemRecyclerImgViewerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: ImageOrder) {
            binding.imageOrder = data
            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ImageOrder>() {
            override fun areItemsTheSame(oldItem: ImageOrder, newItem: ImageOrder): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ImageOrder,
                newItem: ImageOrder
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}