package com.finpo.app.ui.community_detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityDetailBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityDetailFragment : BaseFragment<FragmentCommunityDetailBinding>(R.layout.fragment_community_detail) {
    private val viewModel by viewModels<CommunityDetailViewModel>()
    private val args by navArgs<CommunityDetailFragmentArgs>()

    private lateinit var writingAdapter: WritingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.detailId = args.id
        viewModel.getWritingDetail()
        viewModel.getComment()
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        writingAdapter = WritingAdapter()
        binding.rvCommunityDetail.adapter = ConcatAdapter(writingAdapter)
        binding.rvCommunityDetail.itemAnimator = null

        viewModel.writingContent.observe(viewLifecycleOwner) {
            writingAdapter.data = it
            writingAdapter.notifyItemChanged(0)
        }
    }
}