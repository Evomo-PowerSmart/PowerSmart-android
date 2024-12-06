package com.evomo.powersmart.di

import com.evomo.powersmart.data.location_data.LocationDataRepository
import com.evomo.powersmart.data.location_data.api.LocationDataApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationDataRepositoryModule {

    @Provides
    fun provideLocationDataRepository(
        locationDataApiService: LocationDataApiService,
    ): LocationDataRepository = LocationDataRepository(
        locationDataApiService = locationDataApiService
    )
}