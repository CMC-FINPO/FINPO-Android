package com.finpo.app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.finpo.app.R
import com.finpo.app.databinding.FragmentHomeBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun init() {
//        val itemList = listOf("최신순")
//        val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner_sort, itemList)
//        binding.spinner.adapter = adapter

    }
}