package com.finpo.app.ui.interest_setting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentInterestSettingBinding
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.filter.FilterAdapter
import com.finpo.app.utils.GridSpacingItemDecoration
import com.finpo.app.utils.dp
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestSettingFragment : BaseFragment<FragmentInterestSettingBinding>(R.layout.fragment_interest_setting) {
    private val viewModel by viewModels<InterestSettingViewModel>()
    private lateinit var interestEditAdapter: InterestEditAdapter
    private lateinit var purposeAdapter: PurposeAdapter
    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeInterestCategoryData()
        observePurposeData()
        observeGoToMyInfoFragmentEvent()

        observeBackEvent()
        observeClearEvent()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeClearEvent() {
        viewModel.clearEvent.observe {
            showAlertDialog("초기화 하시겠어요?") {
                viewModel.clearData()
                interestEditAdapter.notifyDataSetChanged()
                purposeAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun observeBackEvent() {
        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }
    }

    private fun observeGoToMyInfoFragmentEvent() {
        viewModel.goToMyInfoFragmentEvent.observe {
            startActivity(
                Intent(
                    requireContext(),
                    MainActivity::class.java
                ).apply { putExtra("startId", R.id.myPageFragment) })
            activity?.finish()
        }
    }

    private fun observePurposeData() {
        viewModel.purposeData.observe(viewLifecycleOwner) {
            purposeAdapter.submitList(it)
        }
    }

    private fun observeInterestCategoryData() {
        viewModel.interestCategoryData.observe(viewLifecycleOwner) {
            interestEditAdapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        interestEditAdapter = InterestEditAdapter(viewModel)
        interestEditAdapter.setHasStableIds(true)
        binding.rvInterest.adapter = interestEditAdapter
        binding.rvInterest.itemAnimator = null

        purposeAdapter = PurposeAdapter(viewModel)
        purposeAdapter.setHasStableIds(true)
        binding.rvPurpose.layoutManager = FlexboxLayoutManager(requireActivity(), FlexDirection.ROW)
        binding.rvPurpose.adapter = purposeAdapter
        binding.rvPurpose.itemAnimator = null
    }
}