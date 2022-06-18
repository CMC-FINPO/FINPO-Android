package com.finpo.app.repository

import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.network.ApiService
import javax.inject.Inject

class MyInfoRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getMyInfo() = apiService.getMyInfo()
}