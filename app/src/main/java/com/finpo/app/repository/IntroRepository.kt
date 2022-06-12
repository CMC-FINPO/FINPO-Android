package com.finpo.app.repository

import com.finpo.app.network.ApiServiceWithoutToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class IntroRepository @Inject constructor(private val apiServiceWithoutToken: ApiServiceWithoutToken) {
    suspend fun checkNicknameDuplication(nickname: String) = apiServiceWithoutToken.checkNicknameDuplication(nickname)
    suspend fun getRegionAll() = apiServiceWithoutToken.getRegionAll()
    suspend fun getRegionDetail(parentId: Int) = apiServiceWithoutToken.getRegionDetail(parentId)
    suspend fun loginByKakao(acToken: String) = apiServiceWithoutToken.loginByKakao("Bearer $acToken")
    suspend fun registerByKakao(kakaoAccessToken: String, profileImg: MultipartBody.Part?,data: HashMap<String, RequestBody>)
    = apiServiceWithoutToken.registerByKakao(kakaoAccessToken, profileImg, data)
}