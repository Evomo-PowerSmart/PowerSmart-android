package com.evomo.powersmart.di

import com.evomo.powersmart.data.location_data.LocationDataRepository
import com.evomo.powersmart.data.location_data.api.LocationDataApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationDataRepositoryModule {

    @Provides
    @Singleton
    fun provideLocationDataApiService(
        retrofit: Retrofit,
    ): LocationDataApiService =
        retrofit.create(LocationDataApiService::class.java)

    @Provides
    fun provideLocationDataRepository(
        locationDataApiService: LocationDataApiService,
    ): LocationDataRepository = LocationDataRepository(
        locationDataApiService = locationDataApiService
    )
}