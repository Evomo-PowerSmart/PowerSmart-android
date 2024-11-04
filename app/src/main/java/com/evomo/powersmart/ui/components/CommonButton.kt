package com.evomo.powersmart.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evomo.powersmart.R
import com.evomo.powersmart.ui.theme.PowerSmartTheme
import com.evomo.powersmart.ui.theme.spacing

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
        }
    }
}

@Composable
fun CommonOutlinedButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(24.dp))
            }
            Text(text = text)
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CommonButtonPreview() {
    PowerSmartTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CommonButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Click me",
                    onClick = {}
                )
                CommonOutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Continue with Google",
                    icon = R.drawable.logo_google,
                    onClick = {}
                )
            }
        }
    }
}