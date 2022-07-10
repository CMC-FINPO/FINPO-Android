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
import com.finpo.app.utils.ImageUtils
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker


@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel by viewModels<MyPageViewModel>()

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.alarmClickEvent.observe {
            findNavController().navigate(MyPageFragmentDirections.actionMyPageFragmentToAlarmFragment())
        }

        viewModel.profileEditClickEvent.observe {
            val permissionListener: PermissionListener = setPermissionListener()
            createPermission(permissionListener)
        }

        viewModel.settingClickEvent.observe {
            val action = MyPageFragmentDirections.actionMyPageFragmentToSettingFragment(viewModel.oAuthType.value)
            findNavController().navigate(action)
        }

        viewModel.regionClickEvent.observe {
            val action = MyPageFragmentDirections.actionMyPageFragmentToEditRegionFragment(viewModel.nickname.value ?: "")
            findNavController().navigate(action)
        }

        viewModel.participationClickEvent.observe {
            findNavController().navigate(MyPageFragmentDirections.actionMyPageFragmentToParticipationListFragment(viewModel.nickname.value ?: ""))
        }

        viewModel.interestSettingClickEvent.observe {
            val action = MyPageFragmentDirections.actionMyPageFragmentToInterestSettingFragment()
            findNavController().navigate(action)
        }
    }

    private fun setPermissionListener(): PermissionListener {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                showImagePicker()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {}
        }
        return permissionListener
    }

    private fun createPermission(permissionListener: PermissionListener) {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
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