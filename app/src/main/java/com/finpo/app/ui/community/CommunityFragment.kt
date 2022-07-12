package com.finpo.app.ui.community

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.NavGraphDirections
import com.finpo.app.R
import com.finpo.app.databinding.FragmentCommunityBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.home.BottomSheetPolicySortDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {
    private val viewModel by viewModels<CommunityViewModel>()
    private lateinit var communityAdapter: CommunityAdapter

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        communityAdapter = CommunityAdapter(viewModel)
        binding.rvCommunity.adapter = communityAdapter

        viewModel.goToDetailFragmentEvent.observe {
            findNavController().navigate(NavGraphDirections.actionGlobalCommunityDetailFragment(it))
        }

        val bottomDialogFragment = BottomSheetCommunitySortDialog(viewModel)

        viewModel.bottomSheetShowEvent.observe {
            bottomDialogFragment.show(requireActivity().supportFragmentManager, bottomDialogFragment.tag)
        }

        viewModel.bottomSheetDismissEvent.observe {
            bottomDialogFragment.dismiss()
        }

        viewModel.writingList.observe(viewLifecycleOwner) {
            communityAdapter.submitList(it.toMutableList()) {
                if(viewModel.paging.page.value == 1)
                    binding.rvCommunity.scrollToPosition(0)
            }
        }

        viewModel.goToPostFragmentEvent.observe {
            findNavController().navigate(CommunityFragmentDirections.actionCommunityFragmentToCommunityPostFragment())
        }
    }
}