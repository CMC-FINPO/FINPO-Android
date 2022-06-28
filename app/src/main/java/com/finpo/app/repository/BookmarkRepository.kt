package com.finpo.app.repository

import com.finpo.app.model.remote.PolicyId
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun addInterestPolicy(policyId: Int) = apiService.addInterestPolicy(PolicyId(policyId))
    suspend fun deleteInterestPolicy(policyId: Int) = apiService.deleteInterestPolicy(policyId)
}