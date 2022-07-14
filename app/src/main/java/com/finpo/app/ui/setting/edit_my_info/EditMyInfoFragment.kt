package com.finpo.app.ui.setting.edit_my_info

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentEditMyInfoBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMyInfoFragment : BaseFragment<FragmentEditMyInfoBinding>(R.layout.fragment_edit_my_info) {
    private val viewModel by viewModels<EditMyInfoViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.backClickEvent.observe {
            findNavController().popBackStack()
        }

        val datePickerDialog = DatePickerDialog()

        viewModel.defaultInfoLiveData.showDatePickerDialog.observe {
            datePickerDialog.showDatePickerDialog(requireContext(), viewModel)
        }

        binding.cl.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            binding.btnEdit.visibility = if(bottom < oldBottom) View.INVISIBLE
            else View.VISIBLE
        }
    }
}