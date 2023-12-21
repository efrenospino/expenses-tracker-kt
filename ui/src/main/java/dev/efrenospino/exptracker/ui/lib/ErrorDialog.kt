package dev.efrenospino.exptracker.ui.lib

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Outlined.Warning, contentDescription = "Error")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(
                text = dialogText,
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation()
            }) {
                Text(stringResource(id = android.R.string.ok))
            }
        },
    )
}

@Composable
fun RetryDialog(
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
    onCancel: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Outlined.Warning, contentDescription = "Error")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(
                text = dialogText,
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(id = android.R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("Retry")
            }
        },
    )
}

@Preview
@Composable
private fun PreviewErrorDialog() {
    RetryDialog(
        onDismissRequest = { },
        onRetry = { },
        onCancel = { },
        dialogTitle = "Oops!",
        dialogText = "Something went wrong with the operation that you were attempting to perform"
    )
}

@Preview
@Composable
private fun PreviewRetryDialog() {
    ErrorDialog(
        onDismissRequest = { },
        onConfirmation = { },
        dialogTitle = "Oops!",
        dialogText = "Something went wrong with the operation that you were attempting to perform"
    )
}
