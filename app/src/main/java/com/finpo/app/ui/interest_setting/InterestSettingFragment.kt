package com.finpo.app.ui.interest_setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentInterestSettingBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestSettingFragment : BaseFragment<FragmentInterestSettingBinding>(R.layout.fragment_interest_setting) {
    private val viewModel by viewModels<InterestSettingViewModel>()
    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

    }
}