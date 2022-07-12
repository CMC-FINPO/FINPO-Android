package com.finpo.app.model.remote

data class NotificationBody(
    val registrationToken: String?,
    val subscribe: Boolean?
)

data class MyNotificationResponse(
    val data: MyNotification
)

data class MyNotification(
    val subscribe: Boolean,
    val interestCategories: List<InterestCategory>?,
    val interestRegions: List<InterestRegion>?
)

data class InterestCategory(
    val id: Int,
    val category: Category,
    var subscribe: Boolean
)

data class InterestRegion(
    val id: Int,
    val region: RegionInterest,
    var subscribe: Boolean
)

data class MyNotificationBody(
    val subscribe: Boolean?,
    val interestRegions: List<IdSubscribe>?,
    val interestCategories: List<IdSubscribe>?
)

data class IdSubscribe(
    val id: Int,
    val subscribe: Boolean
)

data class NotificationHistoryResponse(
    val data: NotificationHistory
)

data class NotificationHistory(
    val content: List<NotificationHistoryContent>,
    val last: Boolean
)

data class NotificationHistoryContent(
    val id: Int,
    val type: String,
    val policy: NotificationPolicy?,
    val comment: NotificationComment?
)

data class NotificationComment(
    val post: NotificationPost?,
    val createdAt: String
)

data class NotificationPolicy(
    val id: Int,
    val title: String,
    val createdAt: String
)

data class NotificationPost(
    val id: Int,
    val content: String
)