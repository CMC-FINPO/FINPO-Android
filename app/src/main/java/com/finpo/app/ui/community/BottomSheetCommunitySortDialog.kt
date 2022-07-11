package com.finpo.app.ui.community

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import com.finpo.app.databinding.DialogCommunitySortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCommunitySortDialog(val viewModel: CommunityViewModel) : BottomSheetDialogFragment() {
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val binding = DialogCommunitySortBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        dialog.setContentView(binding.root)
    }
}