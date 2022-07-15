package com.finpo.app.ui.community.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerCommunityDetailWritingBinding
import com.finpo.app.model.remote.WritingContent

class WritingAdapter() : RecyclerView.Adapter<WritingAdapter.WritingViewHolder>() {
    var data: WritingContent? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WritingViewHolder {
        return WritingViewHolder(
            ItemRecyclerCommunityDetailWritingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: WritingViewHolder, position: Int) {
        holder.setData(data)
    }

    override fun getItemCount(): Int = 1

    inner class WritingViewHolder(private val binding: ItemRecyclerCommunityDetailWritingBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: WritingContent?) {
            if(data == null) return
            binding.data = data
        }
    }
}