package com.evomo.powersmart.ui.screen.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onLoginClicked() = viewModelScope.launch {
        if (_state.value.email.isBlank() || _state.value.password.isBlank()) {
            _errorMessage.emit("Please fill all the fields!")
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        val loginResult = authRepository.signInWithEmail(
            _state.value.email,
            _state.value.password
        )
        onLoginResult(loginResult)
    }

    fun onContinueWithGoogle(token: String) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val signInResult = authRepository.signInWithGoogle(token)
            onLoginResult(signInResult)
        }
    }

    private fun onLoginResult(result: Resource<SignedInResponse>) {
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