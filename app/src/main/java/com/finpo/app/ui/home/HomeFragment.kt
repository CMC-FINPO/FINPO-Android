package com.finpo.app.ui.home

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentHomeBinding
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var policyAdapter: PolicyAdapter
    private val args: HomeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(args.categories == null || args.regionIds == null || args.regionTextList == null) {
            viewModel.getInitData()
        }
        else {
            viewModel.regionIds = args.regionIds!!.toList()
            viewModel.categoryIds = args.categories!!.toList()
            viewModel.regionTextList = args.regionTextList!!.toList()
            viewModel.changePolicy()
        }
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        observeRecyclerView()

        observeBookmarkCountMax()

        observeGoToFilterFragmentEvent()
        observeGoToDetailFragmentEvent()

        val bottomDialogFragment = BottomSheetPolicySortDialog(viewModel)

        observeBottomSheetShowEvent(bottomDialogFragment)
        observeBottomSheetDismissEvent(bottomDialogFragment)

        observeKeyBoardSearchEvent()

        if((activity as MainActivity).isMovedHomeBySelectedBottomNavigationItem) {
            viewModel.clearPolicy()
            viewModel.changePolicy()
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Pair<Int, Boolean>>("isBookmarked")
            ?.observe(viewLifecycleOwner) {
                viewModel.checkBookmarkChanged(it.first, it.second)
            }
    }

    private fun observeKeyBoardSearchEvent() {
        viewModel.keyBoardSearchEvent.observe {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }
    }

    private fun observeBottomSheetDismissEvent(bottomDialogFragment: BottomSheetPolicySortDialog) {
        viewModel.bottomSheetDismissEvent.observe {
            bottomDialogFragment.dismiss()
        }
    }

    private fun observeBottomSheetShowEvent(bottomDialogFragment: BottomSheetPolicySortDialog) {
        viewModel.bottomSheetShowEvent.observe {
            bottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                bottomDialogFragment.tag
            )
        }
    }

    private fun observeGoToDetailFragmentEvent() {
        viewModel.goToDetailFragmentEvent.observe { id ->
            val action = NavGraphDirections.actionGlobalPolicyDetailFragment(id)
            findNavController().navigate(action)
        }
    }

    private fun observeGoToFilterFragmentEvent() {
        viewModel.goToFilterFragmentEvent.observe {
            val action = HomeFragmentDirections.actionHomeFragmentToFilterFragment(
                viewModel.regionTextList.toTypedArray(),
                viewModel.categoryIds.toIntArray(),
                viewModel.regionIds.toIntArray()
            )
            findNavController().navigate(action)
        }
    }

    private fun observeBookmarkCountMax() {
        viewModel.showBookmarkCountMaxToastEvent.observe {
            longShowToast(getString(R.string.bookmark_max_msg))
        }
    }

    private fun observeRecyclerView() {
        viewModel.policyList.observe(viewLifecycleOwner) {
            policyAdapter.submitList(it.toMutableList()) {
                if (viewModel.paging.page.value == 1 && !viewModel.refreshedByBookmarked) {
                    binding.rvPolicy.scrollToPosition(0)
                } else viewModel.refreshedByBookmarked = false
            }
        }
    }

    private fun initRecyclerView() {
        policyAdapter = PolicyAdapter(viewModel)
        binding.rvPolicy.adapter = policyAdapter
    }
}