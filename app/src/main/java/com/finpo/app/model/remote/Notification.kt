package com.finpo.app.model.remote

data class NotificationBody(
    val registrationToken: String?,
    val subscribe: Boolean?
)