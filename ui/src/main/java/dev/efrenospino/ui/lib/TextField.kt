package dev.efrenospino.ui.lib

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BasicTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        readOnly = readOnly,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        label = {
            Text(text = label)
        },
        onValueChange = onValueChange,
    )
}

@Composable
fun FullWidthTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier, horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = MaterialTheme.typography.displayLarge.copy(
                textAlign = TextAlign.Center
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = placeholder,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.displayLarge.copy(
                        textAlign = TextAlign.Center
                    )
                )
            },
            value = value,
            onValueChange = onValueChange
        )
    }
}