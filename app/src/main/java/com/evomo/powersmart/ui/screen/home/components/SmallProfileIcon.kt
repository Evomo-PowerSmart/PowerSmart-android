package com.evomo.powersmart.ui.screen.home.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.evomo.powersmart.R
import com.evomo.powersmart.ui.theme.PowerSmartTheme

@Composable
fun SmallProfileIcon(
    modifier: Modifier = Modifier,
    photoUrl: String?,
    onClick: () -> Unit,
) {
    if (photoUrl.isNullOrBlank() || photoUrl == "null") {
        Image(
            painter = painterResource(id = R.drawable.ic_profile_stock),
            contentDescription = "Profile Picture Stock",
            modifier = modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable { onClick() },
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            model = photoUrl,
            contentDescription = "Profile Picture",
            modifier = modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable { onClick() },
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SmallProfileIconPreview() {
    PowerSmartTheme {
        SmallProfileIcon(
            modifier = Modifier, photoUrl = null, onClick = {}
        )
    }
}