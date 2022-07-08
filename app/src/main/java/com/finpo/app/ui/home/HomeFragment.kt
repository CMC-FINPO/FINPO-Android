package com.finpo.app.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.R
import com.finpo.app.databinding.FragmentHomeBinding
import com.finpo.app.model.local.IdName
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

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
        val bottomDialogFragment = BottomSheetSortDialog(viewModel)

        policyAdapter = PolicyAdapter(viewModel)
        binding.rvPolicy.adapter = policyAdapter

        viewModel.policyList.observe(viewLifecycleOwner) {
            policyAdapter.submitList(it.toMutableList()) {
                if(viewModel.paging.page.value == 1)
                    binding.rvPolicy.scrollToPosition(0)
            }
        }

        viewModel.goToFilterFragmentEvent.observe {
            val action = HomeFragmentDirections.actionHomeFragmentToFilterFragment(viewModel.regionTextList.toTypedArray(), viewModel.categoryIds.toIntArray(), viewModel.regionIds.toIntArray())
            findNavController().navigate(action)
        }

        viewModel.goToDetailFragmentEvent.observe { id ->
            val action = HomeFragmentDirections.actionHomeFragmentToPolicyDetailFragment(id)
            findNavController().navigate(action)
        }

        viewModel.bottomSheetShowEvent.observe {
            bottomDialogFragment.show(requireActivity().supportFragmentManager, bottomDialogFragment.tag)
        }

        viewModel.bottomSheetDismissEvent.observe {
            bottomDialogFragment.dismiss()
        }

        viewModel.keyBoardSearchEvent.observe {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }

        if((activity as MainActivity).isMovedHomeBySelectedItem) {
            viewModel.clearPolicy()
            viewModel.changePolicy()
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Pair<Int, Boolean>>("isBookmarked")
            ?.observe(viewLifecycleOwner) {
                viewModel.checkBookmarkChanged(it.first, it.second)
            }

        viewModel.updateRecyclerViewItemEvent.observe {
            policyAdapter.notifyItemChanged(it.first, it.second)
        }
    }
}