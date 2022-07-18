package com.finpo.app.repository

import com.finpo.app.model.remote.*
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun postWriting(postWritingRequest: PostWritingRequest) = apiService.postWriting(postWritingRequest)
    suspend fun putWriting(id: Int, postWritingRequest: PostWritingRequest) = apiService.putWriting(id, postWritingRequest)
    suspend fun getWriting(content: String, page: Int, sort: List<String>) = apiService.getWriting(content, page, sort)
    suspend fun getWritingDetail(id: Int) = apiService.getWritingDetail(id)
    suspend fun deleteWriting(id: Int) = apiService.deleteWriting(id)
    suspend fun getComment(id: Int, page: Int) = apiService.getComment(id, page)
    suspend fun postComment(id: Int, comment: String) = apiService.postComment(id, CommentRequest(comment))
    suspend fun deleteComment(id: Int) = apiService.deleteComment(id)
    suspend fun editComment(id: Int, comment: String) = apiService.editComment(id, Content(comment))
    suspend fun postCommentReply(postId: Int, parentId: Int,comment: String)
    = apiService.postCommentReply(
        postId,
        CommentReplyRequest(CommentReplyParent(parentId), comment)
    )

    suspend fun putWritingBookmark(id: Int) = apiService.putWritingBookmark(id)
    suspend fun deleteWritingBookmark(id: Int) = apiService.deleteWritingBookmark(id)
    suspend fun putWritingLike(id: Int) = apiService.putWritingLike(id)
    suspend fun deleteWritingLike(id: Int) = apiService.deleteWritingLike(id)
}