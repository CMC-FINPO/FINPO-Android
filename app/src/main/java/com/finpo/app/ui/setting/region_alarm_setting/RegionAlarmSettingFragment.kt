package com.finpo.app.ui.setting.region_alarm_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentRegionAlarmSettingBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.setting.interest_alarm_setting.InterestAlarmAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegionAlarmSettingFragment : BaseFragment<FragmentRegionAlarmSettingBinding>(R.layout.fragment_region_alarm_setting) {
    private val viewModel by viewModels<RegionAlarmSettingViewModel>()
    private lateinit var regionAlarmAdapter: RegionAlarmAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        regionAlarmAdapter = RegionAlarmAdapter(viewModel)
        regionAlarmAdapter.setHasStableIds(true)
        binding.rvRegionAlarm.adapter = regionAlarmAdapter

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }

        viewModel.regionAlarmData.observe(viewLifecycleOwner) {
            regionAlarmAdapter.submitList(it.toList())
            regionAlarmAdapter.notifyDataSetChanged()
        }
    }
}