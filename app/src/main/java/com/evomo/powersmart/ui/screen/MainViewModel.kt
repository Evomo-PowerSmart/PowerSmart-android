package com.evomo.powersmart.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evomo.powersmart.data.auth.AuthRepository
import com.evomo.powersmart.data.preferences.PreferenceManager
import com.evomo.powersmart.data.preferences.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager,
) : ViewModel() {

    var splashCondition by mutableStateOf(true)
        private set

    private val _themeValue = MutableStateFlow(Theme.SYSTEM_DEFAULT)
    val themeValue: StateFlow<Theme> = _themeValue.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    init {
        checkUserLoggedIn()
    }

    private fun getTheme() = viewModelScope.launch {
        preferenceManager.getTheme().collect { theme ->
            _themeValue.value = theme
        }
    }

    private fun checkUserLoggedIn() = viewModelScope.launch {
        _isUserLoggedIn.value = authRepository.getSignedInUser().data != null
        splashCondition = false
    }
}