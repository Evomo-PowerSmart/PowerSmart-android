package com.evomo.powersmart.ui.screen.login

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isSuccessful: Boolean = false,
)
