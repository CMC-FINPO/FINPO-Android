package com.finpo.app.di

import android.app.Application
import com.finpo.app.R
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class FinpoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.KAKAO_NATIVE_APP_KEY))
    }

}