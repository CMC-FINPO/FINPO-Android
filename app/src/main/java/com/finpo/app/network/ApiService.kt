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

    @HTTP(method = "DELETE",path = "/user/me", hasBody = true)
    suspend fun withdrawal(
        @Body googleToken: GoogleToken
    ) : ApiResponse<JsonElement>

    @GET("/policy/search")
    suspend fun getPolicy(
        @Query("title") title: String,
        @Query("region", encoded = true) region: List<Int> ,
        @Query("category", encoded = true) category: List<Int>,
        @Query("page") page: Int,
        @Query("sort", encoded = true) sort: List<String>,
        @Query("size") size: Int = 10
    ) : ApiResponse<PolicyResponse>

    @GET("/region/me")
    suspend fun getMyRegion() : ApiResponse<MyRegionResponse>

    @GET("/user/status/name")
    suspend fun getStatusList() : ApiResponse<StatusPurposeResponse>

    @GET("/user/purpose/name")
    suspend fun getPurposeList() : ApiResponse<StatusPurposeResponse>

    @PUT("/user/me")
    suspend fun setStatusPurpose(
        @Body statusPurposeBody: StatusPurposeBody,
    ) : ApiResponse<JsonElement>

    @GET("/policy/category/me")
    suspend fun getMyCategory() : ApiResponse<MyCategoryResponse>
}