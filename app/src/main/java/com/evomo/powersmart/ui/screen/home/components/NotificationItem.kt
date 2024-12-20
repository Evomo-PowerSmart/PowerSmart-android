package com.evomo.powersmart.ui.screen.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    timestamp: Timestamp,
    onClick: () -> Unit,
) {
    OutlinedCard(onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = timestamp.toDate().toFormattedString(),
                fontSize = 16.sp
            )
        }
    }
}

fun Date.toFormattedString(): String {
    val format = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault())
    return format.format(this)
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NotificationItemPreview() {
    PowerSmartTheme {
        Surface {
            Column {
                repeat(3) {
                    NotificationItem(
                        timestamp = Timestamp(Date()),
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}