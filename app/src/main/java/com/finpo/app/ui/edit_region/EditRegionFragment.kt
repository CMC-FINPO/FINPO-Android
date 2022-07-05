package com.finpo.app.ui.edit_region

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentEditRegionBinding
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.format

@AndroidEntryPoint
class EditRegionFragment : BaseFragment<FragmentEditRegionBinding>(R.layout.fragment_edit_region) {
    private val viewModel by viewModels<EditRegionViewModel>()
    private val args by navArgs<EditRegionFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.nickname = args.nickname
        viewModel.getMyInterestRegion()
        viewModel.interestRegionViewModel.MAX_COUNT = MAX_ADDITIONAL_COUNT
        viewModel.livingRegionViewModel.MAX_COUNT = 1
        viewModel.interestRegionViewModel.getRegionDataByRemote()
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

        viewModel.goToMyInfoFragmentEvent.observe {
            startActivity(Intent(requireContext(), MainActivity::class.java).apply { putExtra("startId",R.id.myPageFragment) })
            activity?.finish()
        }

        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }
    }
}