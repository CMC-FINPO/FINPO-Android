package com.finpo.app.ui.edit_region.edit_region_interest

import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentEditRegionInterestBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.edit_region.EditRegionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRegionInterestFragment : BaseFragment<FragmentEditRegionInterestBinding>(R.layout.fragment_edit_region_interest) {
    private val viewModel by viewModels<EditRegionViewModel>({requireParentFragment()})
    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}