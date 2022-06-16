package com.finpo.app.network

import com.finpo.app.model.remote.RegionInterestResponse
import com.finpo.app.model.remote.RegionRequest
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/region/me")
    suspend fun addMyInterestRegion(
        @Body regionList: List<RegionRequest>
    ) : ApiResponse<RegionInterestResponse>
}