package com.finpo.app.ui.edit_region.edit_region_interest

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentEditRegionInterestBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.edit_region.EditRegionViewModel
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.format

@AndroidEntryPoint
class EditRegionInterestFragment : BaseFragment<FragmentEditRegionInterestBinding>(R.layout.fragment_edit_region_interest) {
    private val viewModel by viewModels<EditRegionViewModel>({requireParentFragment()})
    private lateinit var regionAllAdapter: EditInterestRegionAdapter
    private lateinit var regionDetailAdapter: EditInterestRegionDetailAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeRecyclerView()

        viewModel.interestRegionViewModel.chooseMaxToastEvent.observe {
            shortShowToast(format(getString(R.string.can_select_max), MAX_ADDITIONAL_COUNT))
        }

        viewModel.interestRegionViewModel.regionOverlapToastEvent.observe {
            shortShowToast(getString(R.string.overlap_region))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setViewpagerTypeInterest()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRecyclerView() {
        viewModel.interestRegionViewModel.regionData.observe(viewLifecycleOwner) {
            regionAllAdapter.submitList(it.data)
        }

        viewModel.interestRegionViewModel.regionDetailData.observe(viewLifecycleOwner) {
            regionDetailAdapter.submitList(it.data)
            regionAllAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        regionAllAdapter = EditInterestRegionAdapter(viewModel)
        regionAllAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionAll.adapter = regionAllAdapter
        binding.regionRecyclerview.rvRegionAll.itemAnimator = null

        regionDetailAdapter = EditInterestRegionDetailAdapter(viewModel)
        regionDetailAdapter.setHasStableIds(true)
        binding.regionRecyclerview.rvRegionDetail.adapter = regionDetailAdapter
        binding.regionRecyclerview.rvRegionDetail.itemAnimator = null
    }
}