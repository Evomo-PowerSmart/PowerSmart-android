package com.evomo.powersmart.di

import com.evomo.powersmart.data.notification.NotificationTokenRepository
import com.evomo.powersmart.data.notification.api.NotificationTokenApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationTokenModule {

    @Provides
    @Singleton
    fun provideNotificationTokenApiService(
        retrofit: Retrofit,
    ): NotificationTokenApiService =
        retrofit.create(NotificationTokenApiService::class.java)

    @Provides
    @Singleton
    fun provideNotificationTokenRepository(
        notificationTokenApiService: NotificationTokenApiService,
    ): NotificationTokenRepository {
        return NotificationTokenRepository(notificationTokenApiService)
    }
}