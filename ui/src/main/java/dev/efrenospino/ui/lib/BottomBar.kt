package dev.efrenospino.ui.lib

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun singleActionBottomBar(
    buttonText: String,
    onClick: () -> Unit,
) = @Composable {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = onClick,
    ) {
        Text(text = buttonText)
    }
}