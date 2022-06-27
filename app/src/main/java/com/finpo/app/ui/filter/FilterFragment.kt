package com.finpo.app.ui.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentFilterBinding
import com.finpo.app.model.remote.MyRegionResponse
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(R.layout.fragment_filter) {
    private val viewModel by viewModels<FilterViewModel>()
    private lateinit var filterAdapter: FilterAdapter
    private val args: FilterFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getCategory()
        viewModel.setRegion(args.regions)
        viewModel.setCategories(args.categories)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        filterAdapter = FilterAdapter(viewModel)
        filterAdapter.setHasStableIds(true)
        binding.rvFilter.adapter = filterAdapter
        binding.rvFilter.itemAnimator = null

        viewModel.clearEvent.observe {
            clearFilter()
        }

        viewModel.filterCategoryData.observe(viewLifecycleOwner) {
            filterAdapter.submitList(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearFilter() {
        viewModel.setCategories(intArrayOf())
        filterAdapter.notifyDataSetChanged()
        viewModel.setRegion(MyRegionResponse(listOf()))
    }
}