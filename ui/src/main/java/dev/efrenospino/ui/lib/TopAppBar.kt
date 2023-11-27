package dev.efrenospino.ui.lib

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun simpleTopAppBar(
    screenTitle: String,
    onNavigationBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) = @Composable {
    TopAppBar(
        title = {
            Text(text = screenTitle)
        },
        navigationIcon = {
            IconButton(onClick = onNavigationBackClick) {
                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close")
            }
        },
        actions = actions,
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun simpleHomeTopAppBar(screenTitle: String, actions: @Composable RowScope.() -> Unit = {}) =
    @Composable {
        TopAppBar(
            title = {
                Text(text = screenTitle)
            },
            actions = actions,
        )
    }
