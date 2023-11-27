package dev.efrenospino.ui.lib

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.efrenospino.domain.usecases.SortExpensesList
import dev.efrenospino.ui.R

@Composable
fun ColumnScope.ExpensesSortByDropdownMenu(
    selectedOption: SortExpensesList.SortBy = SortExpensesList.SortBy.CREATED,
    onOptionClicked: (SortExpensesList.SortBy) -> Unit = {},
) {
    Text(text = "Sort By", modifier = Modifier.padding(10.dp))
    DropdownMenuItem(
        text = { Text("Date") },
        onClick = { onOptionClicked(SortExpensesList.SortBy.CREATED) },
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Sort by Date")
        },
        trailingIcon = {
            if (selectedOption == SortExpensesList.SortBy.CREATED) {
                Icon(imageVector = Icons.Outlined.Check, contentDescription = "")
            }
        }
    )
    DropdownMenuItem(
        text = { Text("Amount") },
        onClick = { onOptionClicked(SortExpensesList.SortBy.AMOUNT) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.outline_attach_money_24),
                contentDescription = "Sort by Amount"
            )
        },
        trailingIcon = {
            if (selectedOption == SortExpensesList.SortBy.AMOUNT) {
                Icon(imageVector = Icons.Outlined.Check, contentDescription = "")
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
private fun PreviewExpensesSortByDropdownMenu() {
    Column {
        ExpensesSortByDropdownMenu()
    }
}