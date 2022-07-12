package com.finpo.app.repository

import com.finpo.app.model.remote.CommentRequest
import com.finpo.app.model.remote.PolicyId
import com.finpo.app.model.remote.PostWritingRequest
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun postWriting(postWritingRequest: PostWritingRequest) = apiService.postWriting(postWritingRequest)
    suspend fun getWriting(content: String, page: Int, sort: List<String>) = apiService.getWriting(content, page, sort)
    suspend fun getWritingDetail(id: Int) = apiService.getWritingDetail(id)
    suspend fun getComment(id: Int, page: Int) = apiService.getComment(id, page)
    suspend fun postComment(id: Int, comment: String) = apiService.postComment(id, CommentRequest(comment))
}