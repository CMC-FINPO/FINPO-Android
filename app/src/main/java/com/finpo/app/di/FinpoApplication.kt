package com.finpo.app.di

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ProgressBar
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
        var loadingDialog: Dialog? = null
        lateinit var instance: FinpoApplication
    }

    override fun onCreate() {
        prefs = PrefsManager(applicationContext)
        encryptedPrefs = EncryptedPrefsManger(applicationContext)
        instance = this
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.KAKAO_NATIVE_APP_KEY))
    }

    fun showLoadingDialog(activity: Activity) {
        if(activity.isFinishing || loadingDialog?.isShowing == true)    return
        loadingDialog = Dialog(activity)
        loadingDialog?.apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setDimAmount(0f)
            setContentView(R.layout.dialog_loading)
            setCanceledOnTouchOutside(false)
            setOnCancelListener { activity.onBackPressed() }
            show()
        }
    }

    fun hideLoadingDialog() {
        if(loadingDialog?.isShowing == true)
            loadingDialog?.dismiss()
    }

}