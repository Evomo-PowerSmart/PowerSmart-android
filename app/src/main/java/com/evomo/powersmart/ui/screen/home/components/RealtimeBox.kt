package com.evomo.powersmart.ui.screen.home.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material.icons.outlined.ArrowCircleUp
import androidx.compose.material.icons.outlined.Battery0Bar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evomo.powersmart.ui.screen.home.Status
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.spacing

@Composable
fun RealtimeBox(
    modifier: Modifier = Modifier,
    location: String,
    lastUpdated: String,
    unitDetail: String,
    value: Double,
    status: Status,
    unit: String,
    addedValue: Double,
    previousValueUnit: String = unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.medium)
                    .background(
                        when (status) {
                            Status.DECREASING -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = location,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.75f),
                    text = lastUpdated,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Outlined.Battery0Bar,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                        text = unitDetail,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.extraSmall),
                    text = "${value.formatToTwoDecimal()} $unit",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = when (status) {
                            Status.INCREASING -> Icons.Outlined.ArrowCircleUp
                            Status.DECREASING -> Icons.Outlined.ArrowCircleDown
                            else -> Icons.Outlined.ArrowCircleRight
                        },
                        tint = when (status) {
                            Status.INCREASING -> MaterialTheme.colorScheme.primary
                            Status.DECREASING -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.tertiary
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                        text = "${addedValue.formatToTwoDecimal()} $previousValueUnit",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun Double.formatToTwoDecimal(): String {
    return if (this % 1.0 == 0.0) {
        // Jika tidak ada angka desimal, tampilkan sebagai bilangan bulat
        this.toInt().toString()
    } else {
        // Jika ada angka desimal, batasi hingga dua angka di belakang koma
        String.format("%.2f", this)
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RealtimeBoxPreview() {
    PowerSmartTheme {
        Surface {
            RealtimeBox(
                location = "Panel AC Lantai 1",
                lastUpdated = "30/10/24 - 13:55",
                unitDetail = "Daya Semu per Hour",
                value = 258.01,
                unit = "VAh",
                status = Status.DECREASING,
                addedValue = 254.0
            )
        }
    }
}