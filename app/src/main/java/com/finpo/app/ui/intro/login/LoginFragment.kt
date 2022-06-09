package com.finpo.app.ui.intro.login

import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentLoginBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val viewModel by activityViewModels<IntroViewModel>()
    override fun init() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.loginLiveData.kakaoLoginEvent.observe {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    //TODO 에러 처리
                }
                else if (token != null) {
                    //TODO 로그인 성공 처리
                    //TODO 이미 가입된 회원인 경우 바로 앱 시작
                    //TODO 미회원인 경우 회원가입 절차
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireActivity())) UserApiClient.instance.loginWithKakaoTalk(requireActivity(), callback = callback)
            else UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
        }
    }
}