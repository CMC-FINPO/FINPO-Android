package com.finpo.app.repository

import com.finpo.app.model.remote.NotificationBody
import com.finpo.app.model.remote.PolicyId
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun setNotification(registrationToken: String? = null, subscribe: Boolean? = null)
    = apiService.setNotification(NotificationBody(registrationToken, subscribe))
}