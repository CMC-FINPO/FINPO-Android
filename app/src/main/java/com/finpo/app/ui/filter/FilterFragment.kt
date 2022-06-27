package com.finpo.app.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.finpo.app.R
import com.finpo.app.databinding.FragmentFilterBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.utils.GridSpacingItemDecoration
import com.finpo.app.utils.dp
import com.google.android.flexbox.FlexboxLayoutManager
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
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        filterAdapter = FilterAdapter(viewModel)

        binding.rvFilter.adapter = filterAdapter
        binding.rvFilter.itemAnimator = null

        viewModel.filterCategoryData.observe(viewLifecycleOwner) {
            filterAdapter.submitList(it)
        }
    }
}