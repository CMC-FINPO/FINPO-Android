package com.finpo.app.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import com.finpo.app.databinding.DialogSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSortDialog(val viewModel: HomeViewModel) : BottomSheetDialogFragment() {
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val binding = DialogSortBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        dialog.setContentView(binding.root)
    }
}