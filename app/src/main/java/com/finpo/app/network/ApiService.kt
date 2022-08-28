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
    ): ApiResponse<RegionInterestResponse>

    @PUT("/region/my-default")
    suspend fun editMyLivingRegion(
        @Body regionList: RegionRequest
    ): ApiResponse<JsonElement>

    @GET("/region/me")
    suspend fun getMyRegion(): ApiResponse<MyRegionResponse>

    //USER
    @GET("/user/me")
    suspend fun getMyInfo(): ApiResponse<MyInfoResponse>

    @Multipart
    @POST("/user/me/profile-img")
    suspend fun changeProfileImg(
        @Part profileImg: MultipartBody.Part?
    ): ApiResponse<MyInfoResponse>

    @HTTP(method = "DELETE", path = "/user/me", hasBody = true)
    suspend fun withdrawal(
        @Body googleToken: GoogleToken
    ): ApiResponse<JsonElement>

    @GET("/user/status/name")
    suspend fun getStatusList(): ApiResponse<StatusPurposeResponse>

    @GET("/user/purpose/name")
    suspend fun getPurposeList(): ApiResponse<StatusPurposeResponse>

    @PUT("/user/me")
    suspend fun setStatusPurpose(
        @Body statusPurposeBody: StatusPurposeBody,
    ): ApiResponse<JsonElement>

    @GET("/user/me/purpose")
    suspend fun getMyPurpose(): ApiResponse<MyPurpose>

    @PUT("/user/me")
    suspend fun editMyInfo(
        @Body editMyInfoRequest: EditMyInfoRequest
    )  : ApiResponse<JsonElement>

    //POLICY
    @GET("/policy/search")
    suspend fun getPolicy(
        @Query("title") title: String,
        @Query("region", encoded = true) region: List<Int>,
        @Query("category", encoded = true) category: List<Int>,
        @Query("page") page: Int,
        @Query("sort", encoded = true) sort: List<String>,
        @Query("size") size: Int = 10
    ): ApiResponse<PolicyResponse>

    @GET("/policy/category/me")
    suspend fun getMyCategory(): ApiResponse<MyCategoryResponse>

    @POST("/policy/interest")
    suspend fun addInterestPolicy(
        @Body policyId: PolicyId
    ): ApiResponse<JsonElement>

    @DELETE("/policy/interest/me")
    suspend fun deleteInterestPolicy(
        @Query("policyId") id: Int
    ): ApiResponse<JsonElement>

    @GET("/policy/{id}")
    suspend fun getPolicyDetail(
        @Path("id") id: Int
    ): ApiResponse<PolicyDetailResponse>

    @GET("/policy/category/me/parent")
    suspend fun getMyParentCategory(): ApiResponse<ParentCategoryResponse>

    @PUT("/policy/category/me")
    suspend fun editMyInterestCategory(
        @Body categoryList: List<CategoryRequest>
    ): ApiResponse<JsonElement>

    @POST("/policy/joined")
    suspend fun addParticipationPolicy(
        @Body policyId: PolicyId
    ): ApiResponse<ParticipationPolicyResponse>

    @PUT("/policy/joined/{id}")
    suspend fun editParticipationPolicyMemo(
        @Path("id") id: Int,
        @Body memo: Memo
    ): ApiResponse<JsonElement>

    @GET("/policy/interest/me")
    suspend fun getMyInterestPolicy(): ApiResponse<MyInterestPolicyResponse>

    @GET("/policy/joined/me")
    suspend fun getMyParticipationPolicy(): ApiResponse<MyInterestPolicyResponse>

    @DELETE("/policy/joined/{id}")
    suspend fun deleteParticipationPolicy(
        @Path("id") id: Int
    ): ApiResponse<JsonElement>

    //NOTI
    @PUT("/notification/me")
    suspend fun setNotification(
        @Body notificationBody: NotificationBody
    ): ApiResponse<JsonElement>

    @GET("/notification/me")
    suspend fun getMyNotification(): ApiResponse<MyNotificationResponse>

    @PUT("/notification/me")
    suspend fun putMyNotification(
        @Body myNotificationBody: MyNotificationBody
    ): ApiResponse<JsonElement>

    @GET("/notification/history/me")
    suspend fun getNotificationHistory(
        @Query("lastId") lastId: Int?,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String = "id,desc",
    ): ApiResponse<NotificationHistoryResponse>

    @DELETE("/notification/history/{id}")
    suspend fun deleteNotificationHistory(
        @Path("id") id: Int
    ): ApiResponse<JsonElement>

    //COMMUNITY
    @POST("/post")
    suspend fun postWriting(@Body postWritingRequest: PostWritingRequest): ApiResponse<JsonElement>

    @PUT("/post/{id}")
    suspend fun putWriting(
        @Path("id") id: Int,
        @Body postWritingRequest: PostWritingRequest
    ): ApiResponse<JsonElement>

    @GET("/post/search")
    suspend fun getWriting(
        @Query("content") content: String,
        @Query("page") page: Int,
        @Query("sort", encoded = true) sort: List<String>,
        @Query("size") size: Int = 10
    ): ApiResponse<WritingResponse>

    @GET("/post/{id}")
    suspend fun getWritingDetail(@Path("id") id: Int): ApiResponse<CommunityDetailResponse>

    @GET("/post/{id}/comment")
    suspend fun getComment(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("sort", encoded = true) sort: String = "id,asc",
        @Query("size") size: Int = 10
    ): ApiResponse<CommentResponse>

    @POST("/post/{id}/comment")
    suspend fun postComment(
        @Path("id") id: Int,
        @Body commentRequest: CommentRequest
    ): ApiResponse<PostCommentResponse>

    @POST("/post/{id}/comment")
    suspend fun postCommentReply(
        @Path("id") id: Int,
        @Body commentReplyRequest: CommentReplyRequest
    ): ApiResponse<PostCommentReplyResponse>

    @DELETE("/post/{id}")
    suspend fun deleteWriting(@Path("id") id: Int): ApiResponse<JsonElement>

    @DELETE("/comment/{id}")
    suspend fun deleteComment(@Path("id") id: Int): ApiResponse<JsonElement>

    @PUT("/comment/{id}")
    suspend fun editComment(
        @Path("id") id: Int,
        @Body content: Content
    ): ApiResponse<JsonElement>

    @GET("/post/me")
    suspend fun getMyWriting(
        @Query("page") page: Int,
        @Query("size") size: Int = 10,
        @Query("sort", encoded = true) sort: String = "id,desc"
    ) : ApiResponse<WritingResponse>

    @GET("/post/comment/me")
    suspend fun getMyComment(
        @Query("page") page: Int,
        @Query("size") size: Int = 10,
        @Query("sort", encoded = true) sort: String = "id,desc"
    ) : ApiResponse<WritingResponse>

    @GET("/post/bookmark/me")
    suspend fun getMyWritingBookmark(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort", encoded = true) sort: String = "id,desc"
    ) : ApiResponse<WritingResponse>

    @POST("/post/{id}/bookmark")
    suspend fun putWritingBookmark(
        @Path("id") id: Int
    ) : ApiResponse<JsonElement>

    @DELETE("/post/{id}/bookmark")
    suspend fun deleteWritingBookmark(
        @Path("id") id: Int
    ) : ApiResponse<JsonElement>

    @POST("/post/{id}/like")
    suspend fun putWritingLike(
        @Path("id") id: Int
    ) : ApiResponse<JsonElement>

    @DELETE("/post/{id}/like")
    suspend fun deleteWritingLike(
        @Path("id") id: Int
    ) : ApiResponse<JsonElement>

    //REPORT
    @GET("/report/reason")
    suspend fun getReportReason(): ApiResponse<ReportReasonResponse>

    @POST("/comment/{id}/report")
    suspend fun reportComment(
        @Path("id") id: Int,
        @Body reportRequest: ReportRequest
    ) : ApiResponse<JsonElement>

    @POST("/post/{id}/report")
    suspend fun reportPost(
        @Path("id") id: Int,
        @Body reportRequest: ReportRequest
    ) : ApiResponse<JsonElement>

    //BLOCK
    @POST("/post/{id}/block")
    suspend fun blockPost(
        @Path("id") id: Int
    ) : ApiResponse<JsonElement>

    @POST("/comment/{id}/block")
    suspend fun blockComment(
        @Path("id") id: Int
    ) : ApiResponse<JsonElement>

    //ETC
    @GET("/information/open-api")
    suspend fun getOpenApiList() : ApiResponse<OpenApiResponse>
}