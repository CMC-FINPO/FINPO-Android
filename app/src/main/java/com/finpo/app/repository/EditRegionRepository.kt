package com.finpo.app.repository

import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditRegionRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun editMyInterestRegion(regionList: List<RegionRequest>) = apiService.editMyInterestRegion(regionList)
    suspend fun editMyLivingRegion(regionId: Int) = apiService.editMyLivingRegion(RegionRequest(regionId))
}