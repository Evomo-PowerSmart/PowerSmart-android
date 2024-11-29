package com.evomo.powersmart.ui.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.evomo.powersmart.BuildConfig
import com.evomo.powersmart.data.Resource
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import timber.log.Timber
import java.security.MessageDigest
import java.util.UUID

suspend fun getGoogleIdToken(context: Context): Resource<String> {
    try {
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        // Idk why this is not working
//        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
//            .setFilterByAuthorizedAccounts(false)
//            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
//            .setNonce(hashedNonce)
//            .build()

        val signInWithGoogleOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(BuildConfig.WEB_CLIENT_ID)
                .setNonce(hashedNonce)
                .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
//            .addCredentialOption(googleIdOption)
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = credentialManager.getCredential(
            context = context,
            request = request
        )

        val credential = result.credential

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

        return Resource.Success(googleIdTokenCredential.idToken)
    } catch (e: CreateCredentialCancellationException) {
        //do nothing, the user chose not to save the credential
        Timber.tag("Credential").v("User cancelled the save: $e")
        return Resource.Error("User cancelled the save")
    } catch (e: CreateCredentialException) {
        Timber.tag("Credential").v("Credential save error: $e")
        return Resource.Error("Credential save error")
    } catch (e: GetCredentialCancellationException) {
        Timber.tag("Credential").v("User cancelled the get credential: $e")
        return Resource.Error("User cancelled the get credential")
    } catch (e: Exception) {
        Timber.tag("Credential").e("Error getting credential: $e")
        return Resource.Error("Error getting credential")
    }
}