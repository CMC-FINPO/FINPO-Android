package com.finpo.app.repository

import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PolicyRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getPolicy(page: Int, sort: List<String>) = apiService.getPolicy(page, sort)
}