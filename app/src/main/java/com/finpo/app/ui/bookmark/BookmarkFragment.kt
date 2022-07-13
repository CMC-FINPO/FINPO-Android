package com.finpo.app.ui.bookmark

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentBookmarkBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {
    private val viewModel by viewModels<BookmarkViewModel>()
    @Inject lateinit var interestCategoryAdapter: InterestCategoryAdapter
    private lateinit var interestPolicyAdapter: InterestPolicyAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getInitData()

        initRecyclerView()
        observeRecyclerView()
        observeGotoDetailFragmentEvent()
    }

    private fun observeGotoDetailFragmentEvent() {
        viewModel.goToDetailFragmentEvent.observe { id ->
            findNavController().navigate(NavGraphDirections.actionGlobalPolicyDetailFragment(id))
        }
    }

    private fun observeRecyclerView() {
        viewModel.categoryData.observe(viewLifecycleOwner) {
            interestCategoryAdapter.submitList(it)
        }

        viewModel.policyList.observe(viewLifecycleOwner) {
            interestPolicyAdapter.submitList(it.toList())
        }
    }

    private fun initRecyclerView() {
        binding.rvUserCategory.adapter = interestCategoryAdapter
        interestPolicyAdapter = InterestPolicyAdapter(viewModel)
        interestPolicyAdapter.setHasStableIds(true)
        binding.rvPolicy.adapter = interestPolicyAdapter
        binding.rvPolicy.itemAnimator = null
    }
}