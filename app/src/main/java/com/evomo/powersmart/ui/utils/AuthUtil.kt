package com.evomo.powersmart.ui.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.evomo.powersmart.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import timber.log.Timber
import java.security.MessageDigest
import java.util.UUID

suspend fun getGoogleIdToken(context: Context): String? {
    try {
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            context = context,
            request = request
        )

        val credential = result.credential

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

        return googleIdTokenCredential.idToken
    } catch (e: CreateCredentialCancellationException) {
        //do nothing, the user chose not to save the credential
        Timber.tag("Credential").v("User cancelled the save: $e")
        return null
    } catch (e: CreateCredentialException) {
        Timber.tag("Credential").v("Credential save error: $e")
        return null
    } catch (e: GetCredentialCancellationException) {
        Timber.tag("Credential").v("User cancelled the get credential: $e")
        return null
    } catch (e: Exception) {
        Timber.tag("Credential").e("Error getting credential: $e")
        return null
    }
}