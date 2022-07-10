package com.finpo.app.ui.alarm

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
            Log.d("deleteAlarm", "adapter에 추가 $it")
            alarmAdapter.submitList(it.toMutableList()) {
                if(viewModel.paging.page.value == 1)
                    binding.rvAlarm.scrollToPosition(0)

                try {
                    val lastVisibleItemPosition =
                        (binding.rvAlarm.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    if (it[lastVisibleItemPosition] == null) viewModel.addHistory()
                } catch (e: Exception) {}
            }
        }

        viewModel.deleteBtnClickEvent.observe {
            alarmAdapter.notifyDataSetChanged()
        }

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }
    }
}