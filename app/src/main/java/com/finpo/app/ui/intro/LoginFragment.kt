package com.finpo.app.ui.intro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentLoginBinding
import com.finpo.app.ui.common.BaseFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val viewModel by activityViewModels<IntroViewModel>()
    override fun init() {
        binding.viewModel = viewModel
        viewModel.kakaoLoginEvent.observe {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    //TODO 에러 처리
                }
                else if (token != null) {
                    //TODO 로그인 성공 처리
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireActivity())) UserApiClient.instance.loginWithKakaoTalk(requireActivity(), callback = callback)
            else UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
        }
    }
}