package com.finpo.app.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.finpo.app.R
import com.finpo.app.databinding.ActivitySplashBinding
import com.finpo.app.di.FinpoApplication
import com.finpo.app.repository.IntroRepository
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.common.BaseActivity
import com.finpo.app.ui.intro.IntroActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var introRepository: IntroRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(IO).launch {
            delay(500L)
            val accessToken = FinpoApplication.encryptedPrefs.getAccessToken()
            val refreshToken = FinpoApplication.encryptedPrefs.getRefreshToken()
            if(accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                goToIntroActivity()
                return@launch
            }

            val refreshTokenResponse = introRepository.refreshToken(accessToken, refreshToken)
            if(refreshTokenResponse.isSuccessful && refreshTokenResponse.body() != null) {
                FinpoApplication.encryptedPrefs.saveAccessToken(refreshTokenResponse.body()!!.data.accessToken!!)
                FinpoApplication.encryptedPrefs.saveRefreshToken(refreshTokenResponse.body()!!.data.refreshToken!!)
                goToMainActivity()
                return@launch
            }

            goToIntroActivity()
        }
    }

    private suspend fun goToIntroActivity() {
        withContext(Main) {
            startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            finish()
        }
    }

    private suspend fun goToMainActivity() {
        withContext(Main) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}