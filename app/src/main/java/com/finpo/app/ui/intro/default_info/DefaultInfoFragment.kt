package com.finpo.app.ui.intro.default_info

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentDefaultInfoBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DefaultInfoFragment :
    BaseFragment<FragmentDefaultInfoBinding>(R.layout.fragment_default_info) {
    private val viewModel by activityViewModels<IntroViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val datePickerDialog = DatePickerDialog()

        viewModel.defaultInfoLiveData.showDatePickerDialog.observe {
            datePickerDialog.showDatePickerDialog(requireContext(), viewModel)
        }

        viewModel.defaultInfoLiveData.isFemaleRadioButtonChecked.observe(viewLifecycleOwner) { isChecked ->
            if (isChecked) viewModel.defaultInfoLiveData.gender = getString(R.string.female_eng)
        }

        viewModel.defaultInfoLiveData.isMaleRadioButtonChecked.observe(viewLifecycleOwner) { isChecked ->
            if (isChecked) viewModel.defaultInfoLiveData.gender = getString(R.string.male_eng)
        }
    }
}