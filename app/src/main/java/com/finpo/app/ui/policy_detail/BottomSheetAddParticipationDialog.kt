package com.finpo.app.ui.policy_detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import com.finpo.app.databinding.DialogAddParticipationBinding
import com.finpo.app.databinding.DialogSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAddParticipationDialog(val viewModel: PolicyDetailViewModel) : BottomSheetDialogFragment() {
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val binding = DialogAddParticipationBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        (dialog as BottomSheetDialog).behavior.isDraggable = false
    }
}