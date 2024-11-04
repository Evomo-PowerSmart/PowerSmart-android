package com.evomo.powersmart.ui.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun commonTopAppBarColor(): TopAppBarColors {
    return TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
    )
}