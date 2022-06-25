package com.finpo.app.ui.intro.status_purpose

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.finpo.app.R
import com.finpo.app.databinding.FragmentStatusPurposeBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.utils.GridSpacingItemDecoration
import com.finpo.app.utils.dp
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

        statusAdapter.setHasStableIds(true)
        binding.rvStatus.layoutManager = FlexboxLayoutManager(requireActivity())
        binding.rvStatus.addItemDecoration(GridSpacingItemDecoration(3, 12.dp, false))
        binding.rvStatus.adapter = statusAdapter
        binding.rvStatus.itemAnimator = null

        purposeAdapter.setHasStableIds(true)
        binding.rvPurpose.layoutManager = FlexboxLayoutManager(requireActivity())
        binding.rvPurpose.addItemDecoration(GridSpacingItemDecoration(3, 12.dp, false))
        binding.rvPurpose.adapter = purposeAdapter
        binding.rvPurpose.itemAnimator = null

        viewModel.statusPurposeLiveData.statusData.observe(viewLifecycleOwner) {
            statusAdapter.submitList(it)
        }

        viewModel.statusPurposeLiveData.statusClickEvent.observe {
            statusAdapter.notifyDataSetChanged()
        }

        viewModel.statusPurposeLiveData.purposeData.observe(viewLifecycleOwner) {
            purposeAdapter.submitList(it)
        }
    }
}