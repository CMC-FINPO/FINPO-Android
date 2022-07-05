package com.finpo.app.repository

import com.finpo.app.model.remote.Memo
import com.finpo.app.model.remote.PolicyId
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PolicyDetailRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getPolicyDetail(id: Int) = apiService.getPolicyDetail(id)
    suspend fun addParticipationPolicy(policyId: Int) = apiService.addParticipationPolicy(PolicyId(policyId))
    suspend fun editParticipationPolicyMemo(id: Int, memo: String) = apiService.editParticipationPolicyMemo(id, Memo(memo))
}