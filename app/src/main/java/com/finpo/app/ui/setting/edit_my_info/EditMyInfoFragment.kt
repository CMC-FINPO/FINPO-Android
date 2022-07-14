package com.finpo.app.ui.setting.edit_my_info

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentEditMyInfoBinding
import com.finpo.app.ui.common.BaseFragment

class EditMyInfoFragment : BaseFragment<FragmentEditMyInfoBinding>(R.layout.fragment_edit_my_info) {
    private val viewModel by viewModels<EditMyInfoViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.backClickEvent.observe {
            findNavController().popBackStack()
        }
    }
}