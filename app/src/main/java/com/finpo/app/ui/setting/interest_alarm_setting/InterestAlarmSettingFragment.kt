package com.finpo.app.ui.setting.interest_alarm_setting

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentInterestAlarmSettingBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InterestAlarmSettingFragment : BaseFragment<FragmentInterestAlarmSettingBinding>(R.layout.fragment_interest_alarm_setting) {
    private val viewModel by viewModels<InterestAlarmSettingViewModel>()
    private lateinit var interestAlarmAdapter: InterestAlarmAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        interestAlarmAdapter = InterestAlarmAdapter(viewModel)
        interestAlarmAdapter.setHasStableIds(true)
        binding.rvInterestAlarm.adapter = interestAlarmAdapter

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }

        viewModel.interestAlarmData.observe(viewLifecycleOwner) {
            interestAlarmAdapter.submitList(it.toList())
            interestAlarmAdapter.notifyDataSetChanged()
        }
    }
}