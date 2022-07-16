package com.finpo.app.ui.setting.report_reason

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentReportReasonBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportReasonFragment : BaseFragment<FragmentReportReasonBinding>(R.layout.fragment_report_reason) {
    private val viewModel by viewModels<ReportReasonViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.backClickEvent.observe {
            findNavController().popBackStack()
        }
    }
}