package com.finpo.app.ui.intro.status_purpose

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.finpo.app.R
import com.finpo.app.databinding.FragmentStatusPurposeBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.utils.GridSpacingItemDecoration
import com.finpo.app.utils.dp
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatusPurposeFragment : BaseFragment<FragmentStatusPurposeBinding>(R.layout.fragment_status_purpose) {
    private val viewModel by activityViewModels<IntroViewModel>()
    @Inject lateinit var statusAdapter: StatusAdapter
    @Inject lateinit var purposeAdapter: PurposeAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeRecyclerView()
        observeStatusClickEvent()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeStatusClickEvent() {
        viewModel.statusPurposeLiveData.statusClickEvent.observe {
            statusAdapter.notifyDataSetChanged()
        }
    }

    private fun observeRecyclerView() {
        viewModel.statusPurposeLiveData.statusData.observe(viewLifecycleOwner) {
            statusAdapter.submitList(it)
        }

        viewModel.statusPurposeLiveData.purposeData.observe(viewLifecycleOwner) {
            purposeAdapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        statusAdapter.setHasStableIds(true)
        binding.rvStatus.layoutManager = FlexboxLayoutManager(requireActivity(), FlexDirection.ROW)
        binding.rvStatus.adapter = statusAdapter
        binding.rvStatus.itemAnimator = null

        purposeAdapter.setHasStableIds(true)
        binding.rvPurpose.layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        binding.rvPurpose.adapter = purposeAdapter
        binding.rvPurpose.itemAnimator = null
    }
}