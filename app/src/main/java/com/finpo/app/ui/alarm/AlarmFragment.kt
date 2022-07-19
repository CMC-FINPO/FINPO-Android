package com.finpo.app.ui.alarm

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentAlarmBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.home.PolicyAdapter
import com.finpo.app.utils.FCM_TYPE.POLICY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {
    private val viewModel by viewModels<AlarmViewModel>()
    private lateinit var alarmAdapter: AlarmAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeAlarmItemClickEvent()
        observeRecyclerViewItem()
        observeDeleteBtnClickEvent()
        observeBackEvent()
    }

    private fun observeBackEvent() {
        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeDeleteBtnClickEvent() {
        viewModel.deleteBtnClickEvent.observe {
            alarmAdapter.notifyDataSetChanged()
        }
    }

    private fun observeRecyclerViewItem() {
        viewModel.historyList.observe(viewLifecycleOwner) {
            alarmAdapter.submitList(it.toMutableList()) {
                if (viewModel.paging.page.value == 1)
                    binding.rvAlarm.scrollToPosition(0)

                //item 삭제하는 경우 recyclerview가 마지막 아이템이 보여지는지 감지를 하지 못해 아래의 코드를 추가함
                try {
                    val lastVisibleItemPosition =
                        (binding.rvAlarm.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    if (it[lastVisibleItemPosition] == null) viewModel.addHistory()
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun observeAlarmItemClickEvent() {
        viewModel.alarmClickEvent.observe { data ->
            when (data.type) {
                POLICY -> findNavController().navigate(
                    NavGraphDirections.actionGlobalPolicyDetailFragment(
                        data.policy?.id ?: 0
                    )
                )
                else -> findNavController().navigate(
                    NavGraphDirections.actionGlobalCommunityDetailFragment(
                        data.comment?.post?.id ?: 0
                    )
                )
            }
        }
    }

    private fun initRecyclerView() {
        alarmAdapter = AlarmAdapter(viewModel)
        binding.rvAlarm.adapter = alarmAdapter
        binding.rvAlarm.itemAnimator = null
    }
}