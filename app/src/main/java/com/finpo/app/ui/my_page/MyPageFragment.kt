package com.finpo.app.ui.my_page

import android.Manifest
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finpo.app.R
import com.finpo.app.databinding.FragmentMyPageBinding
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.policy_detail.PolicyDetailViewPagerAdapter
import com.finpo.app.utils.ImageUtils
import com.finpo.app.utils.PermissionManager
import com.google.android.material.tabs.TabLayoutMediator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import javax.inject.Inject


@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel by viewModels<MyPageViewModel>()
    @Inject lateinit var permissionManager: PermissionManager

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initViewPager()

        observeGoToDetailFragmentEvent()
        observeAlarmClickEvent()
        observeProfileEditClickEvent()
        observeSettingClickEvent()
        observeRegionClickEvent()
        observeParticipationClickEvent()
        observeInterestSettingClickEvent()
    }

    private fun observeInterestSettingClickEvent() {
        viewModel.interestSettingClickEvent.observe {
            val action = MyPageFragmentDirections.actionMyPageFragmentToInterestSettingFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeParticipationClickEvent() {
        viewModel.participationClickEvent.observe {
            findNavController().navigate(
                MyPageFragmentDirections.actionMyPageFragmentToParticipationListFragment(
                    viewModel.nickname.value ?: ""
                )
            )
        }
    }

    private fun observeRegionClickEvent() {
        viewModel.regionClickEvent.observe {
            val action = MyPageFragmentDirections.actionMyPageFragmentToEditRegionFragment(
                viewModel.nickname.value ?: ""
            )
            findNavController().navigate(action)
        }
    }

    private fun observeSettingClickEvent() {
        viewModel.settingClickEvent.observe {
            val action =
                MyPageFragmentDirections.actionMyPageFragmentToSettingFragment(viewModel.oAuthType.value)
            findNavController().navigate(action)
        }
    }

    private fun observeProfileEditClickEvent() {
        viewModel.profileEditClickEvent.observe {
            val permissionListener: PermissionListener = permissionManager.setPermissionListener { showImagePicker() }
            permissionManager.createGetImagePermission(permissionListener)
        }
    }

    private fun observeAlarmClickEvent() {
        viewModel.alarmClickEvent.observe {
            findNavController().navigate(MyPageFragmentDirections.actionMyPageFragmentToAlarmFragment())
        }
    }

    private fun observeGoToDetailFragmentEvent() {
        viewModel.goToDetailFragmentEvent.observe {
            findNavController().navigate(
                MyPageFragmentDirections.actionGlobalCommunityDetailFragment(
                    it,
                    true
                )
            )
        }
    }

    private fun initViewPager() {
        binding.viewPagerMyPage.adapter = MyPageViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tbMyPage, binding.viewPagerMyPage) { tab, position ->
            tab.text = when (position) {
                0 -> "내가 쓴 글"
                1 -> "댓글 단 글"
                else -> "스크랩 한 글"
            }
        }.attach()
    }

    private fun showImagePicker() {
        TedBottomPicker.with(requireActivity())
            .setPreviewMaxCount(Int.MAX_VALUE)
            .show { uri ->
                val bitmap = ImageUtils().uriToBitmap(requireActivity(), uri)
                viewModel.changeProfileImg(bitmap)
            }
    }
}