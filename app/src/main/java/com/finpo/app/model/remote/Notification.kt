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
    val subscribe: Boolean
)