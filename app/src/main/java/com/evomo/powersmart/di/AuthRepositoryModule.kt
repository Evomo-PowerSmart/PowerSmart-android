package com.evomo.powersmart.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.evomo.powersmart.data.auth.AuthRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    fun provideAuthRepository(
        @ApplicationContext context: Context,
    ): AuthRepository = AuthRepository(
        credentialManager = CredentialManager.create(context),
        auth = Firebase.auth
    )
}