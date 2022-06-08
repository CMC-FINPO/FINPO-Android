package com.finpo.app.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finpo.app.R
import com.finpo.app.databinding.FragmentTermsConditionsBinding
import com.finpo.app.ui.common.BaseFragment

class TermsConditionsFragment : BaseFragment<FragmentTermsConditionsBinding>(R.layout.fragment_terms_conditions) {
    override fun init() {
        binding.lifecycleOwner = viewLifecycleOwner
    }
}