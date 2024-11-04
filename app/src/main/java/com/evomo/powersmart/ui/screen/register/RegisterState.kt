package com.evomo.powersmart.ui.screen.register

data class RegisterState(
    val isLoading: Boolean = false,
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isSuccessful: Boolean = false,
)
