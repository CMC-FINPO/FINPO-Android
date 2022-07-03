package com.finpo.app.ui.edit_region.edit_region_living

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentEditRegionLivingBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.edit_region.EditRegionViewModel

class EditRegionLivingFragment : BaseFragment<FragmentEditRegionLivingBinding>(R.layout.fragment_edit_region_living) {
    private val viewModel by viewModels<EditRegionViewModel>({requireParentFragment()})
    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onResume() {
        super.onResume()
        viewModel.setViewpagerTypeLiving()
    }
}