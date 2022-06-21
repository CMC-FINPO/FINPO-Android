package com.finpo.app.repository

import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.network.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyInfoRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getMyInfo() = apiService.getMyInfo()
    suspend fun changeProfileImg(profileImg: MultipartBody.Part?) = apiService.changeProfileImg(profileImg)
}