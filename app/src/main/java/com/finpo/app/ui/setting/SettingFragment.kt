package com.finpo.app.ui.setting

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.mikepenz.aboutlibraries.LibsBuilder
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
        binding.lifecycleOwner = viewLifecycleOwner

        observeEditMyInfoEvent()
        observeReportReasonEvent()
        observeOpenApiEvent()
        observeOpenSourceEvent()
        observeBackEvent()
        observeGoToInterestAlarmSettingFragmentEvent()
        observeGoToRegionAlarmSettingFragmentEvent()
        observeLogoutClickEvent()
        observeWithdrawalClickEvent()
        observeWithdrawalSuccessfulEvent()
    }

    private fun observeWithdrawalSuccessfulEvent() {
        viewModel.withdrawalSuccessfulEvent.observe { statusCode ->
            hideLoadingDialog()
            when (statusCode) {
                200 -> logout()
                202 -> {
                    longShowToast("소셜 계정 해제 실패! 문의하기를 통해 연락주세요!")
                    logout()
                }
                else -> longShowToast("회원 탈퇴 실패! 문의하기를 통해 연락주세요!")
            }
        }
    }

    private fun observeWithdrawalClickEvent() {
        viewModel.withdrawalClickEvent.observe {
            showAlertDialog("회원 탈퇴 하시겠어요?") {
                showLoadingDialog()
                withdrawal()
            }
        }
    }

    private fun observeLogoutClickEvent() {
        viewModel.logoutClickEvent.observe {
            showAlertDialog("로그아웃 하시겠어요?") { logout() }
        }
    }

    private fun observeGoToRegionAlarmSettingFragmentEvent() {
        viewModel.goToRegionAlarmSettingFragmentEvent.observe {
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToRegionAlarmSettingFragment())
        }
    }

    private fun observeGoToInterestAlarmSettingFragmentEvent() {
        viewModel.goToInterestAlarmSettingFragmentEvent.observe {
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToInterestAlarmSettingFragment())
        }
    }

    private fun observeBackEvent() {
        viewModel.backEvent.observe {
            findNavController().popBackStack()
        }
    }

    private fun observeOpenSourceEvent() {
        viewModel.openSourceEvent.observe {
            LibsBuilder().start(requireContext())
        }
    }

    private fun observeOpenApiEvent() {
        viewModel.openApiEvent.observe {
            showConfirmDialog(it)
        }
    }

    private fun observeReportReasonEvent() {
        viewModel.reportReasonEvent.observe {
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToReportReasonFragment())
        }
    }

    private fun observeEditMyInfoEvent() {
        viewModel.editMyInfoEvent.observe {
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToEditMyInfoFragment())
        }
    }

    private fun withdrawal() {
        if (args.oAuthType == getString(R.string.google_eng)) googleWithdrawal()
         else kakaoWithdrawal()
    }

    private fun googleWithdrawal() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.GOOGLE_CLIENT_ID))
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val task = googleSignInClient.silentSignIn()
        task.addOnCompleteListener {
            CoroutineScope(IO).launch {
                val googleAccessToken = viewModel.getGoogleAccessToken(
                    getString(R.string.GOOGLE_CLIENT_ID),
                    getString(R.string.GOOGLE_SECRET_ID),
                    task.result.serverAuthCode ?: ""
                )
                viewModel.withdrawal(googleAccessToken)
            }
        }
    }

    private fun kakaoWithdrawal() {
        CoroutineScope(IO).launch {
            viewModel.withdrawal()
        }
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