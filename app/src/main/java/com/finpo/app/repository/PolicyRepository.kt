package com.finpo.app.repository

import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PolicyRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getPolicy(
        title: String = "",
        region: List<Int> = listOf(),
        category: List<Int> = listOf(),
        page: Int,
        sort: List<String>
    )
    = apiService.getPolicy(title, region, category, page, sort)
}