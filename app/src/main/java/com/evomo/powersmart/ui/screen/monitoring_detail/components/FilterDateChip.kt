package com.evomo.powersmart.ui.screen.monitoring_detail.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.spacing
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDateChip(
    modifier: Modifier = Modifier,
    startDateValue: String?,
    endDateValue: String?,
    onStartDateValueChange: (Long) -> Unit,
    onEndDateValueChange: (Long) -> Unit,
) {
    var isStartDatePickerVisible by remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
    )

    var isEndDatePickerVisible by remember { mutableStateOf(false) }
    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        FilterChip(
            modifier = Modifier.weight(2f),
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = startDateValue ?: "Start Date",
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Start Date"
                )
            },
            selected = false,
            onClick = {
                isStartDatePickerVisible = true
            }
        )
        FilterChip(
            modifier = Modifier.weight(2f),
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = endDateValue ?: "End Date",
                    textAlign = TextAlign.Center
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "End Date"
                )
            },
            selected = false,
            onClick = {
                isEndDatePickerVisible = true
            }
        )
        FilterChip(
            label = {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = "Clear",
                    textAlign = TextAlign.Center
                )
            },
            selected = false,
            onClick = {
                onStartDateValueChange(0)
                onEndDateValueChange(0)
            },
            colors = FilterChipDefaults.filterChipColors().copy(
                containerColor = MaterialTheme.colorScheme.surface,
                labelColor = MaterialTheme.colorScheme.error,
            )
        )
    }

    if (isStartDatePickerVisible || isEndDatePickerVisible) {
        DatePickerDialog(
            onDismissRequest = {
                isStartDatePickerVisible = false
                isEndDatePickerVisible = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (isStartDatePickerVisible) {
                            onStartDateValueChange(startDatePickerState.selectedDateMillis ?: 0)
                        } else {
                            onEndDateValueChange(endDatePickerState.selectedDateMillis ?: 0)
                        }
                        isStartDatePickerVisible = false
                        isEndDatePickerVisible = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isStartDatePickerVisible = false
                        isEndDatePickerVisible = false
                    }
                ) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(
                state = if (isStartDatePickerVisible) startDatePickerState else endDatePickerState,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterDateChipPreview() {
    PowerSmartTheme {
        Surface {
            FilterDateChip(
                startDateValue = "02/01/2021",
                endDateValue = "02/01/2021",
                onStartDateValueChange = {},
                onEndDateValueChange = {},
            )
        }
    }
}