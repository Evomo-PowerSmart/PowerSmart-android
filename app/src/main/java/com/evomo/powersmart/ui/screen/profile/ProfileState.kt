package com.evomo.powersmart.ui.screen.profile

import com.evomo.powersmart.data.auth.model.SignedInResponse

data class ProfileState(
    val isLoading: Boolean = false,
    val userData: SignedInResponse? = null,
    val isLogoutSuccess: Boolean = false,
)
