package com.finpo.app.network

import android.util.Log
import com.finpo.app.di.FinpoApplication
import com.finpo.app.utils.RETROFIT_TAG
import okhttp3.Interceptor
import javax.inject.Inject

class AuthenticationInterceptor@Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val accessToken = FinpoApplication.encryptedPrefs.getAccessToken() ?: ""
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken").build()
        Log.d(
            RETROFIT_TAG,
            "AuthenticationInterceptor - intercept() called / request header: ${request.headers}"
        )
        return chain.proceed(request)
    }
}