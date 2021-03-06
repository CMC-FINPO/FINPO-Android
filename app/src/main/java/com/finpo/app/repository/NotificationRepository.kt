package com.finpo.app.repository

import com.finpo.app.model.remote.IdSubscribe
import com.finpo.app.model.remote.MyNotificationBody
import com.finpo.app.model.remote.NotificationBody
import com.finpo.app.model.remote.PolicyId
import com.finpo.app.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun setNotification(registrationToken: String? = null, subscribe: Boolean? = null)
    = apiService.setNotification(NotificationBody(registrationToken, subscribe))
    suspend fun getMyNotification() = apiService.getMyNotification()
    suspend fun putMyNotification(
        totalSubscribe: Boolean? = null,
        regionSubscribe: List<IdSubscribe>? = null,
        interestSubscribe: List<IdSubscribe>? = null,
        communitySubscribe: Boolean? = null,
        adSubscribe: Boolean? = null
    ) = apiService.putMyNotification(MyNotificationBody(totalSubscribe, regionSubscribe, interestSubscribe, communitySubscribe, adSubscribe))
    suspend fun getNotificationHistory(lastId: Int?) = apiService.getNotificationHistory(lastId = lastId)
    suspend fun deleteNotificationHistory(id: Int) = apiService.deleteNotificationHistory(id)
}