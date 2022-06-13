package com.finpo.app.di

import android.app.Application
import com.finpo.app.R
import com.finpo.app.utils.EncryptedPrefsManger
import com.finpo.app.utils.PrefsManager
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class FinpoApplication : Application() {
    companion object {
        lateinit var prefs: PrefsManager
        lateinit var encryptedPrefs: EncryptedPrefsManger
    }

    override fun onCreate() {
        prefs = PrefsManager(applicationContext)
        encryptedPrefs = EncryptedPrefsManger(applicationContext)
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.KAKAO_NATIVE_APP_KEY))
    }

}