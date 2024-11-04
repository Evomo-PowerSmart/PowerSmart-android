package com.evomo.powersmart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullscreenLoading(modifier: Modifier = Modifier) {
    CompositionLocalProvider(value = LocalRippleConfiguration provides RippleConfiguration(
        color = Color.Transparent,
        rippleAlpha = RippleAlpha(0f, 0f, 0f, 0f)
    )) {
        Box(modifier = modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.3f))
            .clickable(enabled = true) { }
            .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}