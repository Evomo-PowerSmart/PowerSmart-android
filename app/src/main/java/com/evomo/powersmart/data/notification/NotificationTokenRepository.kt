package com.evomo.powersmart.data.notification

import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.data.notification.api.NotificationTokenApiService
import com.evomo.powersmart.data.notification.model.AddNotificationResponse
import com.evomo.powersmart.data.notification.model.TokenRequest
import javax.inject.Inject

class NotificationTokenRepository @Inject constructor(
    private val notificationTokenApiService: NotificationTokenApiService,
) {

    suspend fun addNotificationToken(token: String): Resource<AddNotificationResponse> {
        return try {
            val response = notificationTokenApiService.addNotificationToken(
                tokenRequest = TokenRequest(token)
            )
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Something went wrong!")
        }
    }
}