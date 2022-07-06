package com.finpo.app.ui.interest_setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.databinding.ItemRecyclerEditPurposeBinding
import com.finpo.app.databinding.ItemRecyclerPurposeBinding
import com.finpo.app.databinding.ItemRecyclerStatusBinding
import com.finpo.app.model.remote.StatusPurpose
import javax.inject.Inject

class PurposeAdapter(val viewModel: InterestSettingViewModel) :
    ListAdapter<StatusPurpose, PurposeAdapter.PurposeViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurposeViewHolder {
        return PurposeViewHolder(
            ItemRecyclerEditPurposeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: PurposeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class PurposeViewHolder(private val binding: ItemRecyclerEditPurposeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: StatusPurpose) {
            binding.data = data
            binding.viewModel = viewModel
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<StatusPurpose>() {
            override fun areContentsTheSame(oldItem: StatusPurpose, newItem: StatusPurpose) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: StatusPurpose, newItem: StatusPurpose) =
                oldItem.id == newItem.id
        }
    }

}