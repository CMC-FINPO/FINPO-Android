package com.finpo.app.network

import com.finpo.app.model.remote.NicknameDuplicationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/user/check-duplicate")
    suspend fun checkNicknameDuplication(
        @Query("nickname") nickname: String
    ) : Response<NicknameDuplicationResponse>
}