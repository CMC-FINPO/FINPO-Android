package com.finpo.app.repository

import com.finpo.app.network.ApiService
import com.finpo.app.network.GoogleLoginApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleLoginRepository @Inject constructor(private val googleLoginApi: GoogleLoginApi) {
    suspend fun getGoogleAccessToken(
        clientId: String,
        clientSecret: String,
        authCode: String,
    ) = googleLoginApi.getAccessTokenGoogle(clientId, clientSecret, authCode)
}