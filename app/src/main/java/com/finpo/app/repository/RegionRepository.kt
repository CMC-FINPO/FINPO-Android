package com.finpo.app.repository

import com.finpo.app.network.ApiServiceWithoutToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionRepository @Inject constructor(private val apiServiceWithoutToken: ApiServiceWithoutToken) {
    suspend fun getRegionAll() = apiServiceWithoutToken.getRegionAll()
    suspend fun getRegionDetail(parentId: Int) = apiServiceWithoutToken.getRegionDetail(parentId)
}