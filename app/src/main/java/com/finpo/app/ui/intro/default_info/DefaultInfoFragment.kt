package com.finpo.app.ui.intro.default_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentDefaultInfoBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DefaultInfoFragment : BaseFragment<FragmentDefaultInfoBinding>(R.layout.fragment_default_info) {
    private val viewModel by activityViewModels<IntroViewModel>()
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}