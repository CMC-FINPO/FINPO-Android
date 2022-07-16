package com.finpo.app.repository

import com.finpo.app.model.remote.CategoryRequest
import com.finpo.app.model.remote.EditMyInfoRequest
import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.network.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyInfoRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getMyInfo() = apiService.getMyInfo()
    suspend fun changeProfileImg(profileImg: MultipartBody.Part?) = apiService.changeProfileImg(profileImg)
    suspend fun getMyRegion() = apiService.getMyRegion()
    suspend fun getMyCategory() = apiService.getMyCategory()
    suspend fun getMyParentCategory() = apiService.getMyParentCategory()
    suspend fun editMyCategory(categoryList: List<CategoryRequest>) = apiService.editMyInterestCategory(categoryList)
    suspend fun getMyInterestPolicy() = apiService.getMyInterestPolicy()
    suspend fun getMyParticipationPolicy() = apiService.getMyParticipationPolicy()
    suspend fun deleteMyParticipationPolicy(id: Int) = apiService.deleteParticipationPolicy(id)
    suspend fun getMyPurpose() = apiService.getMyPurpose()
    suspend fun editMyInfo(
        name: String? = null,
        nickname: String? = null,
        birth: String? = null,
        gender: String? = null
    ) = apiService.editMyInfo(EditMyInfoRequest(name, nickname, birth, gender))
    suspend fun getMyWriting(page: Int) = apiService.getMyWriting(page)
    suspend fun getMyComment(page: Int) = apiService.getMyComment(page)
    suspend fun getMyWritingBookmark() = apiService.getMyWritingBookmark()
}