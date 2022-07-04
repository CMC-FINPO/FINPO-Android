package com.finpo.app.ui.bookmark

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentBookmarkBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {
    private val viewModel by viewModels<BookmarkViewModel>()
    @Inject lateinit var interestCategoryAdapter: InterestCategoryAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvUserCategory.adapter = interestCategoryAdapter

        viewModel.categoryData.observe(viewLifecycleOwner) {
            interestCategoryAdapter.submitList(it)
        }
    }
}