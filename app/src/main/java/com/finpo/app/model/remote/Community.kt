package com.finpo.app.model.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PostWritingRequest(
    val content: String,
    val anonymity: Boolean = false,
    val imgs: List<ImageOrder>? = null
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
    var content: String,
    val anonymity: Boolean,
    var likes: Int,
    var hits: Int,
    var countOfComment: Int,
    val user: WritingUser?,
    val isUserWithdraw: Boolean,
    val isMine: Boolean?,
    var isLiked: Boolean?,
    var isBookmarked: Boolean?,
    var isModified: Boolean?,
    val isUserBlocked: Boolean?,
    val createdAt: String,
    var modifiedAt: String,
    val imgs: List<ImageOrder>?
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

data class CommentReplyRequest(
    val parent: CommentReplyParent,
    val content: String,
    val anonymity: Boolean = false
)

data class CommentReplyParent(
    val id: Int
)

data class PostCommentResponse(
    val data: CommentContent
)

data class PostCommentReplyResponse(
    val data: CommentChilds
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
    val modifiedAt: String?,
    val anonymity: Boolean,
    val anonymityId: Int?,
    var childs: MutableList<CommentChilds>? = mutableListOf()
)

data class CommentChilds(
    var status: Boolean,
    val id: Int,
    val parent: CommentParent,
    val content: String,
    val user: WritingUser?,
    val anonymity: Boolean,
    val anonymityId: Int,
    val isWriter: Boolean?,
    val isMine: Boolean?,
    val isModified: Boolean?,
    val isUserWithdraw: Boolean?,
    val isBlocked: Boolean?,
    val createdAt: String?,
    val modifiedAt: String?
)

data class CommentParent(
    val status: Boolean,
    var id: Int
)

data class Content(
    val content: String
)

data class CommunityImageResponse(
    val data : CommunityImage
)

data class CommunityImage(
    val imgUrls: List<String>
)

@Parcelize
data class ImageOrder(
    val img: String,
    val order: Int
) : Parcelable