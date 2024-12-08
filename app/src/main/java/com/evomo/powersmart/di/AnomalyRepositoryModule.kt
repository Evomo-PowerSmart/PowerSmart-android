package com.evomo.powersmart.di

import com.evomo.powersmart.api.ApiService
import com.evomo.powersmart.data.anomaly.AnomalyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AnomalyRepositoryModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://34.27.207.135/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideAnomalyRepository(apiService: ApiService): AnomalyRepository {
        return AnomalyRepository(apiService)
    }
}