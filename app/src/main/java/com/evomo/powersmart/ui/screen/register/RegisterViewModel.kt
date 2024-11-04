package com.evomo.powersmart.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.data.auth.AuthRepository
import com.evomo.powersmart.data.auth.model.SignedInResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun onFullNameChanged(fullName: String) {
        _state.update { it.copy(fullName = fullName) }
    }

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onRegisterClicked() = viewModelScope.launch {
        if (_state.value.fullName.isBlank() || _state.value.email.isBlank() || _state.value.password.isBlank()) {
            _errorMessage.emit("Please fill all the fields!")
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val registerResult = authRepository.registerWithEmail(
            _state.value.fullName,
            _state.value.email,
            _state.value.password
        )
        onRegisterResult(registerResult)
    }

    fun onContinueWithGoogle(token: String) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val signInResult = authRepository.signInWithGoogle(token)
            onRegisterResult(signInResult)
        }
    }

    private fun onRegisterResult(result: Resource<SignedInResponse>) {
        Timber.e("onSignInResult: ${result.data}")
        viewModelScope.launch {
            when (result) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isSuccessful = false,
                        isLoading = false,
                    )
                    _errorMessage.emit(
                        result.message ?: "Something went wrong!"
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isSuccessful = true,
                        isLoading = false,
                    )
                }
            }
        }
    }
}