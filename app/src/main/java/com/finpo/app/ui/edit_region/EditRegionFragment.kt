package com.finpo.app.ui.edit_region

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentEditRegionBinding
import com.finpo.app.ui.common.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditRegionFragment : BaseFragment<FragmentEditRegionBinding>(R.layout.fragment_edit_region) {
    private val viewModel by viewModels<EditRegionViewModel>()
    private val args by navArgs<EditRegionFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.nickname = args.nickname
        viewModel.getMyInterestRegion()
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewPagerEditRegion.adapter = EditRegionViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tbEditRegion, binding.viewPagerEditRegion) {  tab, position ->
            tab.text = when(position) {
                0 -> "관심 지역"
                else -> "거주 지역"
            }
        }.attach()
    }
}