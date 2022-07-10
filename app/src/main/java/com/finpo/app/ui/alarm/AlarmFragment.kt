package com.finpo.app.ui.alarm

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentAlarmBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.home.PolicyAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {
    private val viewModel by viewModels<AlarmViewModel>()
    private lateinit var alarmAdapter: AlarmAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        alarmAdapter = AlarmAdapter(viewModel)
        binding.rvAlarm.adapter = alarmAdapter

        viewModel.historyList.observe(viewLifecycleOwner) {
            alarmAdapter.submitList(it.toMutableList()) {
                if(viewModel.paging.page.value == 1)
                    binding.rvAlarm.scrollToPosition(0)
            }
        }

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }
    }
}