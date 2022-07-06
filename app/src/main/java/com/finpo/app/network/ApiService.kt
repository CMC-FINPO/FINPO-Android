package com.finpo.app.network

import com.finpo.app.model.remote.*
import com.google.android.gms.common.api.Api
import com.google.gson.JsonElement
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    //REGION
    @PUT("/region/me")
    suspend fun editMyInterestRegion(
        @Body regionList: List<RegionRequest>
    ) : ApiResponse<RegionInterestResponse>

    @PUT("/region/my-default")
    suspend fun editMyLivingRegion(
        @Body regionList: RegionRequest
    ): ApiResponse<JsonElement>

    @GET("/region/me")
    suspend fun getMyRegion() : ApiResponse<MyRegionResponse>

    //USER
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

    @GET("/user/status/name")
    suspend fun getStatusList() : ApiResponse<StatusPurposeResponse>

    @GET("/user/purpose/name")
    suspend fun getPurposeList() : ApiResponse<StatusPurposeResponse>

    @PUT("/user/me")
    suspend fun setStatusPurpose(
        @Body statusPurposeBody: StatusPurposeBody,
    ) : ApiResponse<JsonElement>

    //POLICY
    @GET("/policy/search")
    suspend fun getPolicy(
        @Query("title") title: String,
        @Query("region", encoded = true) region: List<Int> ,
        @Query("category", encoded = true) category: List<Int>,
        @Query("page") page: Int,
        @Query("sort", encoded = true) sort: List<String>,
        @Query("size") size: Int = 10
    ) : ApiResponse<PolicyResponse>

    @GET("/policy/category/me")
    suspend fun getMyCategory() : ApiResponse<MyCategoryResponse>

    @POST("/policy/interest")
    suspend fun addInterestPolicy(
        @Body policyId: PolicyId
    ) : ApiResponse<JsonElement>

    @DELETE("/policy/interest/me")
    suspend fun deleteInterestPolicy(
        @Query("policyId") id: Int
    ) : ApiResponse<JsonElement>

    @GET("/policy/{id}")
    suspend fun getPolicyDetail(
        @Path("id") id: Int
    ) : ApiResponse<PolicyDetailResponse>

    @GET("/policy/category/me/parent")
    suspend fun getMyParentCategory() : ApiResponse<ParentCategoryResponse>

    @PUT("/policy/category/me")
    suspend fun editMyInterestCategory(
        @Body categoryList: List<CategoryRequest>
    ) : ApiResponse<JsonElement>

    @POST("/policy/joined")
    suspend fun addParticipationPolicy(
        @Body policyId: PolicyId
    ) : ApiResponse<ParticipationPolicyResponse>

    @PUT("/policy/joined/{id}")
    suspend fun editParticipationPolicyMemo(
        @Path("id") id: Int,
        @Body memo: Memo
    ) : ApiResponse<JsonElement>

    //TODO REFACTOR endPoint 변경
    @GET("/policy/interest/me")
    suspend fun getMyInterestPolicy(): ApiResponse<MyInterestPolicyResponse>

    //NOTI
    @PUT("/notification/me")
    suspend fun setNotification(
        @Body notificationBody: NotificationBody
    ) : ApiResponse<JsonElement>

    @GET("/notification/me")
    suspend fun getMyNotification() : ApiResponse<MyNotificationResponse>

    @PUT("/notification/me")
    suspend fun putMyNotification(
        @Body myNotificationBody: MyNotificationBody
    ) : ApiResponse<JsonElement>
}