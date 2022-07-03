package com.finpo.app.repository

import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditRegionRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun addMyInterestRegion(regionList: List<RegionRequest>) = apiService.addMyInterestRegion(regionList)
}