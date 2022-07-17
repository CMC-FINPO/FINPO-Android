package com.finpo.app.model.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class WritingContent(
    val status: Boolean,
    val id: Int,
    val content: String,
    val anonymity: Boolean,
    var likes: Int,
    val hits: Int,
    var countOfComment: Int,
    val user: WritingUser?,
    val isUserWithdraw: Boolean,
    val isMine: Boolean?,
    var isLiked: Boolean?,
    var isBookmarked: Boolean?,
    val isModified: Boolean?,
    val isUserBlocked: Boolean?,
    val createdAt: String,
    val modifiedAt: String,
) : Parcelable

@Parcelize
data class WritingUser(
    val id: Int,
    val nickname: String,
    val profileImg: String?,
    val gender: String = "MALE"
) : Parcelable

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

data class CommentRequest(
    val content: String,
    val anonymity: Boolean = false
)

data class PostCommentResponse(
    val data: CommentContent
)

data class CommentContent(
    val status: Boolean,
    val id: Int,
    val content: String?,
    val user: WritingUser?,
    val isUserWithdraw: Boolean?,
    val isUserBlocked: Boolean?,
    val isModified: Boolean?,
    val isMine: Boolean?,
    val isWriter: Boolean?,
    val createdAt: String?,
    val modifiedAt: String?
)

data class Content(
    val content: String
)