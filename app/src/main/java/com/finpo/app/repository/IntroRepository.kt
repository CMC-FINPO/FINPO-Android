package com.finpo.app.repository

import com.finpo.app.model.remote.RequestTokenBody
import com.finpo.app.model.remote.TokenResponse
import com.finpo.app.network.ApiServiceWithoutToken
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntroRepository @Inject constructor(private val apiServiceWithoutToken: ApiServiceWithoutToken) {
    suspend fun checkNicknameDuplication(nickname: String) =
        apiServiceWithoutToken.checkNicknameDuplication(nickname)

    suspend fun getRegionAll() = apiServiceWithoutToken.getRegionAll()

    suspend fun getRegionDetail(parentId: Int) = apiServiceWithoutToken.getRegionDetail(parentId)

    suspend fun loginByKakao(acToken: String) =
        apiServiceWithoutToken.loginByKakao("Bearer $acToken")

    suspend fun registerByKakao(
        kakaoAccessToken: String,
        profileImg: MultipartBody.Part?,
        data: HashMap<String, RequestBody>
    ) = apiServiceWithoutToken.registerByKakao(kakaoAccessToken, profileImg, data)

    suspend fun loginByGoogle(acToken: String) = apiServiceWithoutToken.loginByGoogle(acToken)

    suspend fun registerByGoogle(
        googleAccessToken: String,
        profileImg: MultipartBody.Part?,
        data: HashMap<String, RequestBody>
    ) = apiServiceWithoutToken.registerByGoogle(googleAccessToken, profileImg, data)

    suspend fun refreshToken(accessToken: String, refreshToken: String) =
        apiServiceWithoutToken.refreshToken(RequestTokenBody(accessToken, refreshToken))

    suspend fun getParentCategory() = apiServiceWithoutToken.getParentCategory()
}