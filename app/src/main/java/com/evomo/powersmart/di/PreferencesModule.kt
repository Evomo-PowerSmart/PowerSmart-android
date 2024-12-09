package com.evomo.powersmart.di

import android.content.Context
import com.evomo.powersmart.data.preferences.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context,
    ): PreferenceManager = PreferenceManager(context)
}