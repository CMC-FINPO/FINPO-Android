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
import com.finpo.app.di.FinpoApplication
import com.finpo.app.model.remote.TokenResponse
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
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
            } else hideLoadingDialog()
        }

    override fun doViewCreated() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeKakaoLoginEvent()
        observeGoogleLoginEvent()
        observeNeedRegisterEvent()
        observeIsLoginSuccessfulEvent()
    }

    private fun observeIsLoginSuccessfulEvent() {
        viewModel.loginLiveData.isLoginSuccessfulEvent.observe(this) { tokenResponse ->
            FinpoApplication.encryptedPrefs.saveTokens(
                tokenResponse.data.accessToken ?: "",
                tokenResponse.data.refreshToken ?: ""
            )
            viewModel.loginLiveData.acToken = ""
            viewModel.setNotification(null)
            hideLoadingDialog()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            activity?.finish()
        }
    }

    private fun observeNeedRegisterEvent() {
        viewModel.loginLiveData.needRegisterEvent.observe(this) { tokenResponse ->
            CoroutineScope(Main).launch {
                setUserInfo(tokenResponse)
                hideLoadingDialog()
                viewModel.nextPage()
            }
        }
    }

    private fun observeGoogleLoginEvent() {
        viewModel.loginLiveData.googleLoginEvent.observe {
            googleLogin()
        }
    }

    private fun observeKakaoLoginEvent() {
        viewModel.loginLiveData.kakaoLoginEvent.observe {
            kakaoLogin()
        }
    }

    private suspend fun setUserInfo(tokenResponse: TokenResponse) {
        with(viewModel.defaultInfoLiveData) {
            gender = tokenResponse.data.gender ?: ""
            isMaleRadioButtonChecked.value = (tokenResponse.data.gender == getString(R.string.male_eng))
            isFemaleRadioButtonChecked.value = (tokenResponse.data.gender == getString(R.string.female_eng))
            nameInputText.value = tokenResponse.data.name ?: ""
            nickNameInputText.value = tokenResponse.data.nickname ?: ""
            setBirth(tokenResponse.data.birth ?: "")
        }
        with(viewModel.loginLiveData) {
            oAuthType = tokenResponse.data.oAuthType ?: ""
            profileImage = ImageUtils().imageUrlToBitmap(requireContext(), tokenResponse.data.profileImg?.replace("http:", "https:"))
        }
    }

    private fun googleLogin() {
        showLoadingDialog()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.GOOGLE_CLIENT_ID))
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        activityLauncher.launch(googleSignInClient.signInIntent)
    }

    private fun kakaoLogin() {
        showLoadingDialog()
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
        if (error != null) hideLoadingDialog()
        else if (token != null) {
            viewModel.loginLiveData.acToken = token.accessToken
            viewModel.loginLiveData.loginFinpoByKakao(token.accessToken)
        }
    }
}