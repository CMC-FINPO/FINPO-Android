package com.finpo.app.repository

import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun withdrawal() = apiService.withdrawal()
}