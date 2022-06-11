package com.finpo.app.repository

import com.finpo.app.network.ApiService
import javax.inject.Inject

class IntroRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun checkNicknameDuplication(nickname: String) = apiService.checkNicknameDuplication(nickname)
    suspend fun getRegionAll() = apiService.getRegionAll()
}