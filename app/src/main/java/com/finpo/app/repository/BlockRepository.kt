package com.finpo.app.repository

import com.finpo.app.model.remote.*
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun blockComment(commentId: Int) = apiService.blockComment(commentId)
    suspend fun blockPost(postId: Int) = apiService.blockPost(postId)
}