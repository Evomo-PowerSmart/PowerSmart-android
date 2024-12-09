package com.evomo.powersmart.di

import com.evomo.powersmart.data.anomaly.AnomalyRepository
import com.evomo.powersmart.data.anomaly.api.AnomalyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AnomalyRepositoryModule {

    @Provides
    fun provideAnomalyApiService(retrofit: Retrofit): AnomalyApiService {
        return retrofit.create(AnomalyApiService::class.java)
    }

    @Provides
    fun provideAnomalyRepository(anomalyApiService: AnomalyApiService): AnomalyRepository {
        return AnomalyRepository(anomalyApiService)
    }
}