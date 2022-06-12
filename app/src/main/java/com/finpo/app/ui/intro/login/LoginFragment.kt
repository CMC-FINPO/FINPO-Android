package com.finpo.app.ui.intro.login

import android.util.Log
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentLoginBinding
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.utils.GlideUtils
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val TAG = "LoginFragment"
    private val viewModel by activityViewModels<IntroViewModel>()
    override fun init() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
            } else {
                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.loginLiveData.kakaoLoginEvent.observe {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    //TODO 에러 처리
                } else if (token != null) {
                    //TODO 서버 api 변경된 후 수정 필요
                    UserApiClient.instance.me { user, error ->
                        CoroutineScope(Main).launch {
                            viewModel.defaultInfoLiveData.nickNameInputText.value =
                                user?.kakaoAccount?.profile?.nickname ?: ""
                            withContext(IO) {
                                viewModel.loginLiveData.profileImage =
                                    GlideUtils(requireContext()).imageUrlToBitmap(user?.kakaoAccount?.profile?.thumbnailImageUrl)
                            }
                            viewModel.loginLiveData.acToken = token.accessToken
                            viewModel.loginLiveData.loginFinpoByKakao(token.accessToken)
                        }
                    }
                    //TODO 로그인 성공 처리
                    //TODO 이미 가입된 회원인 경우 바로 앱 시작
                    //TODO 미회원인 경우 회원가입 절차
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireActivity())) UserApiClient.instance.loginWithKakaoTalk(
                requireActivity(),
                callback = callback
            )
            else UserApiClient.instance.loginWithKakaoAccount(
                requireActivity(),
                callback = callback
            )
        }
    }
}