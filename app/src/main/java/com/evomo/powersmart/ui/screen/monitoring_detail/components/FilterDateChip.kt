package com.evomo.powersmart.ui.screen.monitoring_detail.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
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
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.spacing
import java.time.Instant
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDateChip(
    modifier: Modifier = Modifier,
    startDateValue: String?,
    endDateValue: String?,
    startTimeValue: String?,
    endTimeValue: String?,
    onStartDateValueChange: (Long) -> Unit,
    onEndDateValueChange: (Long) -> Unit,
    onStartTimeValueChange: (Long) -> Unit,
    onEndTimeValueChange: (Long) -> Unit,
    onApplyClick: () -> Unit,
) {
    var isStartDatePickerVisible by remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
    )

    var isEndDatePickerVisible by remember { mutableStateOf(false) }
    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
    )

    var isStartTimePickerVisible by remember { mutableStateOf(false) }
    val startTimePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = true
    )

    var isEndTimePickerVisible by remember { mutableStateOf(false) }
    val endTimePickerState = rememberTimePickerState(
        initialHour = 23,
        initialMinute = 59,
        is24Hour = true
    )

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    text = "Start Date",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                FilterChip(
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
            }
            Column(
                modifier = Modifier.weight(2f),
            ) {
                Text(
                    text = "End Date",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                FilterChip(
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
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            FilterChip(
                modifier = Modifier.weight(2f),
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = startTimeValue ?: "Start Time",
                        textAlign = TextAlign.Center
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Start Time"
                    )
                },
                selected = false,
                onClick = {
                    isStartTimePickerVisible = true
                }
            )
            FilterChip(
                modifier = Modifier.weight(2f),
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = endTimeValue ?: "End Time",
                        textAlign = TextAlign.Center
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "End Time"
                    )
                },
                selected = false,
                onClick = {
                    isEndTimePickerVisible = true
                }
            )

        }

        FilterChip(
            label = {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = "Apply",
                    textAlign = TextAlign.Center
                )
            },
            selected = false,
            onClick = onApplyClick,
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

    if (isStartTimePickerVisible || isEndTimePickerVisible) {
        TimePickerDialog(
            onDismissRequest = {
                isStartTimePickerVisible = false
                isEndTimePickerVisible = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (isStartTimePickerVisible) {
                            val timeinMillis = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, startTimePickerState.hour)
                                set(Calendar.MINUTE, startTimePickerState.minute)
                                set(Calendar.SECOND, 0)
                            }.timeInMillis
                            onStartTimeValueChange(timeinMillis)
                        } else {
                            val timeinMillis = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, endTimePickerState.hour)
                                set(Calendar.MINUTE, endTimePickerState.minute)
                                set(Calendar.SECOND, 59)
                            }.timeInMillis
                            onEndTimeValueChange(timeinMillis)
                        }
                        isStartTimePickerVisible = false
                        isEndTimePickerVisible = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isStartTimePickerVisible = false
                        isEndTimePickerVisible = false
                    }
                ) {
                    Text("Cancel")
                }
            },
        ) {
            TimePicker(state = if (isStartTimePickerVisible) startTimePickerState else endTimePickerState)
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
                startTimeValue = "12:00:00",
                endTimeValue = "11:59:59",
                onStartDateValueChange = {},
                onEndDateValueChange = {},
                onStartTimeValueChange = {},
                onEndTimeValueChange = {},
                onApplyClick = {}
            )
        }
    }
}