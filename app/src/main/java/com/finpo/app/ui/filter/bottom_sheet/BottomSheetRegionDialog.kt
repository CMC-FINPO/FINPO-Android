package com.finpo.app.ui.filter.bottom_sheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.finpo.app.databinding.DialogRegionBinding
import com.finpo.app.ui.filter.FilterViewModel
import com.finpo.app.utils.dp
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetRegionDialog(val viewModel: FilterViewModel) : BottomSheetDialogFragment() {
    lateinit var binding: DialogRegionBinding
    private lateinit var editRegionAdapter: EditRegionAdapter
    private lateinit var editRegionDetailAdapter: EditRegionDetailAdapter
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = DialogRegionBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initRecyclerView()
        initData()
        initDialog(dialog)
        observeRecyclerView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRecyclerView() {
        viewModel.bottomSheetRegionViewModel.bottomFilterRegionViewModel.regionData.observe(this) {
            editRegionAdapter.submitList(it.data)
        }

        viewModel.bottomSheetRegionViewModel.bottomFilterRegionViewModel.regionDetailData.observe(this) {
            editRegionAdapter.notifyDataSetChanged()
            editRegionDetailAdapter.submitList(it.data)
        }
    }

    private fun initData() {
        viewModel.bottomSheetRegionViewModel.initEditRegionSelTextList(
            viewModel.filterRegionViewModel.regionTextList, viewModel.filterRegionViewModel.regionIds
        )
        viewModel.bottomSheetRegionViewModel.bottomFilterRegionViewModel.getRegionDataByRemote()
    }

    private fun initDialog(dialog: Dialog) {
        (dialog as BottomSheetDialog).behavior.peekHeight = 800.dp
        dialog.behavior.isDraggable = false
        dialog.setContentView(binding.root)
    }


    private fun initRecyclerView() {
        editRegionAdapter = EditRegionAdapter(viewModel)
        editRegionDetailAdapter = EditRegionDetailAdapter(viewModel)

        editRegionAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionAll.adapter = editRegionAdapter
        binding.regionRecyclerview.rvRegionAll.itemAnimator = null

        editRegionDetailAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionDetail.adapter = editRegionDetailAdapter
        binding.regionRecyclerview.rvRegionDetail.itemAnimator = null
    }
}