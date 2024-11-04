package com.evomo.powersmart.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evomo.powersmart.ui.theme.PowerSmartTheme

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    placeHolder: String,
    leadingIcon: ImageVector?,
    value: String,
    onValueChanged: (value: String) -> Unit,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChanged,
        shape = RoundedCornerShape(12.dp),
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            if (value.isNotBlank()) {
                IconButton(
                    onClick = {
                        onValueChanged("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        placeholder = {
            Text(text = placeHolder, color = Color(0xFFB4AFAF))
        },
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CommonTextFieldPreview() {
    PowerSmartTheme {
        Surface {
            var value by remember { mutableStateOf("") }

            CommonTextField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.Default.Person,
                placeHolder = "Name",
                value = value,
                onValueChanged = { value = it },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
        }
    }
}