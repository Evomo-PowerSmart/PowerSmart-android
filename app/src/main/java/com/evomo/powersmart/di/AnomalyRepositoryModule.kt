package com.evomo.powersmart.di

import com.evomo.powersmart.data.anomaly.AnomalyRepository
import com.evomo.powersmart.data.anomaly.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AnomalyRepositoryModule {

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideAnomalyRepository(apiService: ApiService): AnomalyRepository {
        return AnomalyRepository(apiService)
    }
}