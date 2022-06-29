package com.finpo.app.repository

import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PolicyDetailRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getPolicyDetail(id: Int) = apiService.getPolicyDetail(id)
}