package com.finpo.app.network

import com.finpo.app.model.remote.NicknameDuplicationResponse
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.model.remote.RequestTokenBody
import com.finpo.app.model.remote.TokenResponse
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceWithoutToken {
    @GET("/user/check-duplicate")
    suspend fun checkNicknameDuplication(
        @Query("nickname") nickname: String
    ) : ApiResponse<NicknameDuplicationResponse>

    @GET("/region/name")
    suspend fun getRegionAll() : ApiResponse<RegionResponse>

    @GET("/region/name")
    suspend fun getRegionDetail(
        @Query("parentId") parentId: Int
    ) : ApiResponse<RegionResponse>

    @GET("/oauth/login/kakao")
    suspend fun loginByKakao(
        @Header("Authorization") kakaoAccessToken: String
    ) : ApiResponse<TokenResponse>

    @Multipart
    @POST("/oauth/register/kakao")
    suspend fun registerByKakao(
        @Header("Authorization") kakaoAccessToken: String,
        @Part profileImg: MultipartBody.Part?,
        @PartMap data: HashMap<String, RequestBody>
    ) : ApiResponse<TokenResponse>

    @POST("/oauth/reissue")
    suspend fun refreshToken(
        @Body requestTokenBody: RequestTokenBody
    ) : ApiResponse<TokenResponse>
}