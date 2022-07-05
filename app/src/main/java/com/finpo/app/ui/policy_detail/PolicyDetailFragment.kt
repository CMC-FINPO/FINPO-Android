package com.finpo.app.ui.policy_detail

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentPolicyDetailBinding
import com.finpo.app.di.FinpoApplication
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.home.BottomSheetSortDialog
import com.finpo.app.utils.dp
import com.google.android.material.tabs.TabLayoutMediator
import com.skydoves.balloon.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyDetailFragment : BaseFragment<FragmentPolicyDetailBinding>(R.layout.fragment_policy_detail) {
    private val viewModel by viewModels<PolicyDetailViewModel>()
    private val args: PolicyDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPolicyDetail(args.id)
    }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if(FinpoApplication.prefs.getBoolean("showBalloon", true)) {
            showBalloon()
            FinpoApplication.prefs.setBoolean("showBalloon", false)
        }

        val bottomDialogFragment = BottomSheetAddParticipationDialog(viewModel)

        viewModel.showBottomDialogEvent.observe {
            bottomDialogFragment.show(requireActivity().supportFragmentManager, bottomDialogFragment.tag)
        }

        viewModel.dismissBottomDialogEvent.observe {
            bottomDialogFragment.dismiss()
        }

        viewModel.overlapParticipationEvent.observe {
            longShowToast("이미 참여 목록에 추가되어있는 정책입니다!")
        }

        viewModel.backClickEvent.observe {
            findNavController().popBackStack()
        }

        binding.viewPagerPolicyDetail.adapter = PolicyDetailViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tbPolicyDetail, binding.viewPagerPolicyDetail) {  tab, position ->
            tab.text = when(position) {
                0 -> "사업 내용"
                else -> "신청 방법"
            }
        }.attach()
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