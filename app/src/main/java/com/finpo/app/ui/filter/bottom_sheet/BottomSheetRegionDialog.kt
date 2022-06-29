package com.finpo.app.ui.filter.bottom_sheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.finpo.app.databinding.DialogRegionBinding
import com.finpo.app.ui.filter.FilterDetailAdapter
import com.finpo.app.ui.filter.FilterViewModel
import com.finpo.app.utils.dp
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

        editRegionAdapter = EditRegionAdapter(viewModel)
        editRegionDetailAdapter = EditRegionDetailAdapter(viewModel)

        setRecyclerView()

        dialog.setContentView(binding.root)
        (dialog as BottomSheetDialog).behavior.peekHeight = 800.dp
        dialog.behavior.isDraggable = false
        viewModel.bottomSheetRegionViewModel.initEditRegionSelTextList(
            viewModel.filterRegionSelTextList,
            viewModel.filterRegionSelCount
        )
        viewModel.bottomSheetRegionViewModel.getRegionData()

        viewModel.bottomSheetRegionViewModel.editRegionData.observe(this) {
            editRegionAdapter.submitList(it.data)
        }

        viewModel.bottomSheetRegionViewModel.editRegionDetailData.observe(this) {
            editRegionAdapter.notifyDataSetChanged()
            editRegionDetailAdapter.submitList(it.data)
        }
    }


    private fun setRecyclerView() {
        editRegionAdapter.setHasStableIds(true)
        binding.rvEditRegionAll.adapter = editRegionAdapter
        binding.rvEditRegionAll.itemAnimator = null

        editRegionDetailAdapter.setHasStableIds(true)
        binding.rvEditRegionDetail.adapter = editRegionDetailAdapter
        binding.rvEditRegionDetail.itemAnimator = null
    }
}