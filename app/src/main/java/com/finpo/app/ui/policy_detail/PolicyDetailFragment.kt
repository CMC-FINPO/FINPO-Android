package com.finpo.app.ui.policy_detail

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentPolicyDetailBinding
import com.finpo.app.di.FinpoApplication
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.utils.dp
import com.google.android.material.tabs.TabLayoutMediator
import com.skydoves.balloon.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyDetailFragment : BaseFragment<FragmentPolicyDetailBinding>(R.layout.fragment_policy_detail) {
    private val viewModel by viewModels<PolicyDetailViewModel>()
    private val args: PolicyDetailFragmentArgs by navArgs()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPolicyDetail(args.id)
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        checkShowBalloon()

        val bottomDialogFragment = BottomSheetAddParticipationDialog(viewModel)

        observeShowBottomDialogEvent(bottomDialogFragment)
        observeDismissBottomDialogEvent(bottomDialogFragment)
        observeShowBookmarkCountMaxToastEvent()
        observeShowParticipationCountMaxToastEvent()
        observeAddParticipationMemoSuccessEvent()
        observeOverlapParticipationEvent()
        observeBackClickEvent()
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
        initViewPager()
    }

    private fun initViewPager() {
        binding.viewPagerPolicyDetail.adapter =
            PolicyDetailViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tbPolicyDetail, binding.viewPagerPolicyDetail) { tab, position ->
            tab.text = when (position) {
                0 -> "사업 내용"
                else -> "신청 방법"
            }
        }.attach()
    }

    private fun observeBackClickEvent() {
        viewModel.backClickEvent.observe {
            popBackStack()
        }
    }

    private fun observeOverlapParticipationEvent() {
        viewModel.overlapParticipationEvent.observe {
            longShowToast("이미 참여 목록에 추가되어있는 정책입니다!")
        }
    }

    private fun observeAddParticipationMemoSuccessEvent() {
        viewModel.addParticipationMemoSuccessEvent.observe {
            showConfirmDialog("메모가 등록되었습니다")
        }
    }

    private fun observeShowParticipationCountMaxToastEvent() {
        viewModel.showParticipationCountMaxToastEvent.observe {
            longShowToast(getString(R.string.participation_max_msg))
        }
    }

    private fun observeShowBookmarkCountMaxToastEvent() {
        viewModel.showBookmarkCountMaxToastEvent.observe {
            longShowToast(getString(R.string.bookmark_max_msg))
        }
    }

    private fun observeDismissBottomDialogEvent(bottomDialogFragment: BottomSheetAddParticipationDialog) {
        viewModel.dismissBottomDialogEvent.observe {
            bottomDialogFragment.dismiss()
        }
    }

    private fun observeShowBottomDialogEvent(bottomDialogFragment: BottomSheetAddParticipationDialog) {
        viewModel.showBottomDialogEvent.observe {
            bottomDialogFragment.show(
                requireActivity().supportFragmentManager,
                bottomDialogFragment.tag
            )
        }
    }

    private fun checkShowBalloon() {
        if (FinpoApplication.prefs.getBoolean("showBalloon", true)) {
            showBalloon()
            FinpoApplication.prefs.setBoolean("showBalloon", false)
        }
    }

    private fun popBackStack() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "isBookmarked", Pair(
                args.id,
                viewModel.policyDetailData.value?.isInterest ?: false
            )
        )
        findNavController().popBackStack()
    }

    private fun showBalloon() {
        val balloon = createBalloon(requireContext()) {
            setWidth(145)
            setHeight(65)
            setText(
                "이 정책에 참여했었다면\n" +
                        "참여 버튼을 눌러 추가해보세요!"
            )
            setTextColorResource(R.color.white_w01)
            setTextTypeface(ResourcesCompat.getFont(requireContext(), R.font.notosans_medium)!!)
            setTextSize(10f)
            setArrowSize(10)
            setArrowPosition(0.7f)
            setCornerRadius(8f)
            setBackgroundColorResource(R.color.point_p01)
            setLifecycleOwner(viewLifecycleOwner)
            build()
        }

        balloon.showAlignBottom(binding.ivPlus, -28.dp, 10.dp)
    }
}