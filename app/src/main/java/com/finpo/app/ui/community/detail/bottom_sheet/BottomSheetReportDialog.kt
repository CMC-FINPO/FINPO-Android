package com.finpo.app.ui.community.detail.bottom_sheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import com.finpo.app.databinding.DialogReportBinding
import com.finpo.app.ui.community.detail.CommunityDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetReportDialog(val viewModel: CommunityDetailViewModel) : BottomSheetDialogFragment() {
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val binding = DialogReportBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        dialog.setContentView(binding.root)

        viewModel.getReportReason()
        val reportAdapter = ReportAdapter(viewModel)
        binding.rvReport.adapter = reportAdapter
        binding.rvReport.itemAnimator = null

        viewModel.reportReason.observe(this) {
            reportAdapter.submitList(it)
        }
    }
}