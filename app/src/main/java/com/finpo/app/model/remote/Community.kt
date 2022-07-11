package com.finpo.app.model.remote

data class PostWritingRequest(
    val content: String,
    val anonymity: Boolean = false
)

data class WritingResponse(
    val data: Writing
)

data class Writing(
    val content: List<WritingContent>,
    val last: Boolean,
    val totalElements: Int
)

data class WritingContent(
    val status: Boolean,
    val id: Int,
    val content: String,
    val anonymity: Boolean,
    val likes: Int,
    val hits: Int,
    val countOfComment: Int,
    val user: WritingUser?,
    val isUserWithdraw: Boolean,
    val isMine: Boolean?,
    val isLiked: Boolean?,
    val isBookmarked: Boolean?,
    val isModified: Boolean?,
    val createdAt: String,
    val modifiedAt: String,
)

data class WritingUser(
    val id: Int,
    val nickname: String,
    val profileImg: String?,
    val gender: String = "MALE"
)

data class CommunityDetailResponse(
    val data: WritingContent
)

data class CommentResponse(
    val data: Comment
)

data class Comment(
    val content: List<CommentContent>,
    val last: Boolean
)

data class CommentContent(
    val status: Boolean,
    val id: Int,
    val content: String?,
    val user: WritingUser,
    val isUserWithdraw: Boolean?,
    val isModified: Boolean?,
    val isWriter: Boolean?,
    val createdAt: String?,
    val modifiedAt: String?
)