package com.finpo.app.network

import com.finpo.app.model.remote.*
import com.google.gson.JsonElement
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("/region/me")
    suspend fun addMyInterestRegion(
        @Body regionList: List<RegionRequest>
    ) : ApiResponse<RegionInterestResponse>

    @GET("/user/me")
    suspend fun getMyInfo() : ApiResponse<MyInfoResponse>

    @Multipart
    @POST("/user/me/profile-img")
    suspend fun changeProfileImg(
        @Part profileImg: MultipartBody.Part?
    ) : ApiResponse<MyInfoResponse>

    @DELETE("/user/me")
    suspend fun withdrawal() : ApiResponse<JsonElement>

    @GET("/policy/me")
    suspend fun getPolicy(
        @Query("page") page: Int,
        @Query("sort[]") sort: List<String>,
        @Query("size") size: Int = 10
    ) : ApiResponse<PolicyResponse>
}