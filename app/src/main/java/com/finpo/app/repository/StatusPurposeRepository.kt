package com.finpo.app.repository

import com.finpo.app.model.remote.RequestTokenBody
import com.finpo.app.model.remote.StatusPurpose
import com.finpo.app.model.remote.StatusPurposeBody
import com.finpo.app.model.remote.TokenResponse
import com.finpo.app.network.ApiService
import com.finpo.app.network.ApiServiceWithoutToken
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatusPurposeRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getStatusList() = apiService.getStatusList()

    suspend fun getPurposeList() = apiService.getPurposeList()

    suspend fun setStatusPurpose(
        statusId: Int? = null,
        purposeIds: List<Int>? = null
    ) = apiService.setStatusPurpose(StatusPurposeBody(statusId, purposeIds))
}