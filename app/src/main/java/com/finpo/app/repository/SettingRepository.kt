package com.finpo.app.repository

import com.finpo.app.network.ApiService
import javax.inject.Inject

class SettingRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun withdrawal() = apiService.withdrawal()
}