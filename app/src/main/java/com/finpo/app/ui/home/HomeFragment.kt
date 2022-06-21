package com.finpo.app.ui.home

import android.util.Log
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.R
import com.finpo.app.databinding.FragmentHomeBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var policyAdapter: PolicyAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        policyAdapter = PolicyAdapter(viewModel)
        binding.rvPolicy.adapter = policyAdapter

        val itemList = listOf("최신순", "인기순")
        val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner_sort, itemList)
        binding.spinner.adapter = adapter

        viewModel.policyList.observe(viewLifecycleOwner) {
            policyAdapter.submitList(it.toMutableList()) {
                if(viewModel.paging.page.value == 1)
                    binding.rvPolicy.scrollToPosition(0)
            }
        }

        viewModel.spinnerPosition.observe(viewLifecycleOwner) { currentPosition ->
            if(currentPosition == viewModel.prevSpinnerPosition) return@observe
            viewModel.prevSpinnerPosition = currentPosition
            viewModel.changePolicy()
        }
    }
}