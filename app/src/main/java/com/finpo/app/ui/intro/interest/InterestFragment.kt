package com.finpo.app.ui.intro.interest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.finpo.app.R
import com.finpo.app.databinding.FragmentInterestBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.utils.GridSpacingItemDecoration
import com.finpo.app.utils.dp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InterestFragment : BaseFragment<FragmentInterestBinding>(R.layout.fragment_interest) {
    private val viewModel by activityViewModels<IntroViewModel>()
    @Inject lateinit var interestAdapter: InterestAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvInterest.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvInterest.addItemDecoration(GridSpacingItemDecoration(2, 12.dp, false))
        binding.rvInterest.adapter = interestAdapter

        viewModel.interestLiveData.categoryData.observe(this) {
            interestAdapter.submitList(it.data)
        }

    }
}