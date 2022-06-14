package com.finpo.app.ui.intro.status_purpose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentStatusPurposeBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel

class StatusPurposeFragment : BaseFragment<FragmentStatusPurposeBinding>(R.layout.fragment_status_purpose) {
    private val viewModel by activityViewModels<IntroViewModel>()
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}