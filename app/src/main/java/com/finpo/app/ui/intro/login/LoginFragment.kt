package com.finpo.app.ui.intro.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.finpo.app.R
import com.finpo.app.databinding.FragmentLoginBinding
import com.finpo.app.network.GoogleLoginApi
import com.finpo.app.repository.GoogleLoginRepository
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseFragment
import com.finpo.app.ui.intro.IntroViewModel
import com.finpo.app.utils.ImageUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val viewModel by activityViewModels<IntroViewModel>()

    private val activityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)!!
                viewModel.loginLiveData.getGoogleAccessToken(getString(R.string.GOOGLE_CLIENT_ID), getString(R.string.GOOGLE_SECRET_ID), account.serverAuthCode.orEmpty())
            }
        }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.loginLiveData.kakaoLoginEvent.observe {
            kakaoLogin()
        }

        viewModel.loginLiveData.googleLoginEvent.observe {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.GOOGLE_CLIENT_ID))
                .build()
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

            activityLauncher.launch(googleSignInClient.signInIntent)
        }

        viewModel.loginLiveData.isLoginSuccessfulEvent.observe(this) { tokenResponse ->
            if (tokenResponse.data.accessToken == null) {
                CoroutineScope(Main).launch {
                    //TODO REFACTOR - KAKAO, GOOGLE
                    if(tokenResponse.data.oAuthType == getString(R.string.kakao_eng)) {
                        viewModel.loginLiveData.oAuthType = tokenResponse.data.oAuthType ?: ""
                        viewModel.nextPage()
                        return@launch
                    }
                    viewModel.defaultInfoLiveData.gender = tokenResponse.data.gender ?: ""
                    viewModel.defaultInfoLiveData.isMaleRadioButtonChecked.value = (tokenResponse.data.gender == getString(R.string.male_eng))
                    viewModel.defaultInfoLiveData.isFemaleRadioButtonChecked.value = (tokenResponse.data.gender == getString(R.string.female_eng))
                    viewModel.defaultInfoLiveData.nameInputText.value = tokenResponse.data.name ?: ""
                    viewModel.defaultInfoLiveData.setBirth(tokenResponse.data.birth ?: "")
                    viewModel.loginLiveData.profileImage = ImageUtils().imageUrlToBitmap(requireContext(), tokenResponse.data.profileImg)
                    viewModel.loginLiveData.oAuthType = tokenResponse.data.oAuthType ?: ""
                    viewModel.nextPage()
                }
            } else {
                viewModel.loginLiveData.acToken = ""
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                activity?.finish()
            }
        }

    }

    private fun kakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireActivity())) {
            UserApiClient.instance.loginWithKakaoTalk(
                requireActivity(),
                callback = kakaoLoginCallback()
            )
        } else UserApiClient.instance.loginWithKakaoAccount(
            requireActivity(),
            callback = kakaoLoginCallback()
        )
    }

    private fun kakaoLoginCallback(): (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            //TODO 에러 처리
        } else if (token != null) {
            //TODO 서버 api 변경된 후 수정 필요
            UserApiClient.instance.me { user, error ->
                CoroutineScope(Main).launch {
                    viewModel.defaultInfoLiveData.nickNameInputText.value =
                        user?.kakaoAccount?.profile?.nickname ?: ""
                    //TODO 생년월일
                    user?.kakaoAccount?.gender?.let {
                        viewModel.defaultInfoLiveData.gender = it.toString()
                        viewModel.defaultInfoLiveData.isMaleRadioButtonChecked.value =
                            (it.toString() == getString(R.string.male_eng))
                        viewModel.defaultInfoLiveData.isFemaleRadioButtonChecked.value =
                            (it.toString() == getString(R.string.female_eng))
                    }
                    viewModel.loginLiveData.profileImage =
                        ImageUtils().imageUrlToBitmap(
                            requireContext(),
                            user?.kakaoAccount?.profile?.thumbnailImageUrl
                        )
                    viewModel.loginLiveData.acToken = token.accessToken
                    viewModel.loginLiveData.loginFinpoByKakao(token.accessToken)
                }
            }
        }
    }
}