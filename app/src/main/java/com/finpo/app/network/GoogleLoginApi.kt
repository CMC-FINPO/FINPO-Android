package com.finpo.app.network

import com.finpo.app.model.remote.GoogleToken
import com.skydoves.sandwich.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleLoginApi {
    @FormUrlEncoded
    @POST("/oauth2/v4/token/")
    suspend fun getAccessTokenGoogle(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") authCode: String,
        @Field("grant_type") grantType: String = "authorization_code",
    ): ApiResponse<GoogleToken>
}