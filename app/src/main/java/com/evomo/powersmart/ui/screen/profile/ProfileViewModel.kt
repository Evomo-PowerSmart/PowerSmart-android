package com.evomo.powersmart.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    init {
        getSignedInUser()
    }

    private fun getSignedInUser() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val user = authRepository.getSignedInUser()) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _errorMessage.emit(user.message ?: "Something went wrong!")
                }

                is Resource.Loading -> {
                    _state.value =
                        _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        userData = user.data
                    )
                }
            }
        }
    }

    fun onSignOut() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = authRepository.signOut()) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _errorMessage.emit(result.message ?: "Something went wrong!")
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    delay(1000)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLogoutSuccess = true
                    )
                }
            }
        }
    }
}