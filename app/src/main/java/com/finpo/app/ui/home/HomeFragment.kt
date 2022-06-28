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
        if(args.categories == null || args.regionIds == null || args.regions == null) {
            viewModel.getInitData()
        }
        else {
            viewModel.regionIds = args.regionIds!!.toList()
            viewModel.categoryIds = args.categories!!.toList()
            viewModel.regions = args.regions!!.toList()
            viewModel.changePolicy()
        }
    }

    override fun doViewCreated() {
        try {
            Log.d("filter","homeViewCreated ${viewModel.regions}")
        } catch (e: Exception) {}
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
            Log.d("filter","filter로 전달 ${viewModel.regions}")
            val action = HomeFragmentDirections.actionHomeFragmentToFilterFragment(viewModel.regions.toTypedArray(), viewModel.categoryIds.toIntArray())
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

        viewModel.updateRecyclerViewItemEvent.observe {
            policyAdapter.notifyItemChanged(it.first, it.second)
        }
    }
}