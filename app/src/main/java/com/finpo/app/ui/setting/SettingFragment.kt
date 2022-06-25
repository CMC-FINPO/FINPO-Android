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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    private val viewModel by viewModels<SettingViewModel>()
    private val args: SettingFragmentArgs by navArgs()

    override fun doViewCreated() {
        binding.viewModel = viewModel

        viewModel.logoutClickEvent.observe {
            showAlertDialog("로그아웃", "로그아웃 하시겠어요?") { logout() }
        }

        viewModel.withdrawalClickEvent.observe {
            showAlertDialog("회원 탈퇴", "회원 탈퇴 하시겠어요?") {
                showLoadingDialog()
                withdrawal()
            }
        }

        viewModel.withdrawalSuccessfulEvent.observe { isSuccessful ->
            hideLoadingDialog()
            if (isSuccessful) logout()
            else longShowToast("회원 탈퇴 실패! 문의하기를 통해 연락주세요!")
        }
    }

    private fun withdrawal() {
        //TODO REFACTOR
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.GOOGLE_CLIENT_ID))
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val task = googleSignInClient.silentSignIn()
        task.addOnSuccessListener {
            CoroutineScope(IO).launch {
                val googleAccessToken =
                    if (args.oAuthType == getString(R.string.google_eng))
                        viewModel.getGoogleAccessToken(getString(R.string.GOOGLE_CLIENT_ID), getString(R.string.GOOGLE_SECRET_ID), task.result.serverAuthCode ?: "")
                    else null
                viewModel.withdrawal(googleAccessToken)
            }
        }.addOnFailureListener {
            CoroutineScope(IO).launch {
                viewModel.withdrawal()
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