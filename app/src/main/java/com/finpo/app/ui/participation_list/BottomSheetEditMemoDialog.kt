package com.finpo.app.ui.participation_list

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import com.finpo.app.databinding.DialogAddParticipationBinding
import com.finpo.app.databinding.DialogEditMemoBinding
import com.finpo.app.databinding.DialogSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetEditMemoDialog(val viewModel: ParticipationListViewModel) : BottomSheetDialogFragment() {
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val binding = DialogEditMemoBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        (dialog as BottomSheetDialog).behavior.isDraggable = false
    }
}