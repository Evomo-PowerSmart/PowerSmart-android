package com.evomo.powersmart.data.notification.api

import com.evomo.powersmart.data.notification.model.AddNotificationResponse
import com.evomo.powersmart.data.notification.model.TokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationTokenApiService {
    @POST("api/add_notification_token")
    suspend fun addNotificationToken(@Body tokenRequest: TokenRequest): AddNotificationResponse
}