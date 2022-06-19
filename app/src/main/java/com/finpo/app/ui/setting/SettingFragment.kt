package com.finpo.app.ui.setting

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentSettingBinding
import com.finpo.app.di.FinpoApplication
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroActivity
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    private val viewModel by viewModels<SettingViewModel>()
    private val args: SettingFragmentArgs by navArgs()

    override fun init() {
        binding.viewModel = viewModel

        viewModel.logoutClickEvent.observe {
            showAlertDialog("로그아웃", "로그아웃 하시겠어요?") { logout() }
        }

        viewModel.withdrawalClickEvent.observe {
            showAlertDialog("회원 탈퇴", "회원 탈퇴 하시겠어요?") { viewModel.withdrawal() }
        }

        viewModel.withdrawalSuccessfulEvent.observe { isSuccessful ->
            if(isSuccessful) {
                if(args.oAuthType == "KAKAO")
                    UserApiClient.instance.unlink {  }
                //TODO 구글 로그아웃 추가
                logout()
            }
        }
    }

    private fun showAlertDialog(title: String, message: String, positiveClick: () -> Unit) {
        val builder = AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인") { _, _ ->
                positiveClick()
            }
            .setNegativeButton("취소") { _, _ ->

            }
        builder.show()
    }

    private fun logout() {
        FinpoApplication.encryptedPrefs.saveAccessToken("")
        FinpoApplication.encryptedPrefs.saveRefreshToken("")
        goToIntro()
    }

    private fun goToIntro() {
        startActivity(Intent(requireActivity(), IntroActivity::class.java))
        activity?.finish()
    }
}