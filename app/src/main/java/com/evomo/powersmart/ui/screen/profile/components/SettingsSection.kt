package com.evomo.powersmart.ui.screen.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.evomo.powersmart.data.preferences.Theme

@Composable
fun SettingsSection(
    modifier: Modifier = Modifier,
    themeValue: String,
    onThemeClick: (theme: Theme) -> Unit,
    aboutValue: String,
    onAboutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Box(modifier = Modifier.fillMaxWidth()) {
//            var themeMenu by remember { mutableStateOf(false) }
//
//            DropdownMenu(
//                offset = DpOffset(88.dp, 0.dp),
//                expanded = themeMenu,
//                onDismissRequest = { themeMenu = false }
//            ) {
//                DropdownMenuItem(
//                    text = {
//                        Text(
//                            text = "System Default",
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
//                        )
//                    },
//                    onClick = { themeMenu = false; onThemeClick(Theme.SYSTEM_DEFAULT) },
//                )
//                DropdownMenuItem(
//                    text = {
//                        Text(
//                            text = "Light",
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
//                        )
//                    },
//                    onClick = { themeMenu = false; onThemeClick(Theme.LIGHT) },
//                )
//                DropdownMenuItem(
//                    text = {
//                        Text(
//                            text = "Dark",
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
//                        )
//                    },
//                    onClick = { themeMenu = false; onThemeClick(Theme.DARK) },
//                )
//            }
//
//            SettingItem(
//                modifier = Modifier.animateContentSize(),
//                icon = when (themeValue) {
//                    "Dark" -> {
//                        Icons.Outlined.DarkMode
//                    }
//
//                    "Light" -> {
//                        Icons.Outlined.LightMode
//                    }
//
//                    else -> {
//                        if (isSystemInDarkTheme()) {
//                            Icons.Outlined.DarkMode
//                        } else {
//                            Icons.Outlined.LightMode
//                        }
//                    }
//                },
//                title = "Theme",
//                value = themeValue,
//                onCLick = { themeMenu = true }
//            )
//        }
        SettingItem(
            icon = Icons.Outlined.Info,
            title = "About",
            value = aboutValue,
            onCLick = onAboutClick
        )
    }
}