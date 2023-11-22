package dev.efrenospino.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.ui.R
import dev.efrenospino.ui.lib.shortName
import dev.efrenospino.ui.lib.usd
import java.time.Month
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    HomeWithExpensesList(uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeWithExpensesList(uiState: HomeScreenState) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "My Expenses")
        })
    }, bottomBar = {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onClick = { /*TODO*/ },
        ) {
            Text(text = "Register New Expense")
        }
    }) { innerPadding ->
        HomeScreenScaffoldContent(innerPadding, uiState)
    }
}

@Composable
private fun HomeScreenScaffoldContent(
    innerPadding: PaddingValues,
    uiState: HomeScreenState,
) {
    Column(
        Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MonthExpensesSummary(uiState.thisMonth, uiState.thisMonthSpend)
        if (uiState.expensesList.isEmpty()) {
            Spacer(modifier = Modifier.size(100.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(0.2F),
                    painter = painterResource(id = R.drawable.baseline_money_off_24),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary),
                    alpha = 0.3F,
                    contentDescription = "Nothing spent yet"
                )
                Text(
                    modifier = Modifier.alpha(alpha = 0.6F),
                    text = "Nothing spent yet",
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.tertiary),
                )
            }
        } else {
            Spacer(modifier = Modifier.size(10.dp))
            ExpensesList(uiState.expensesList)
        }
    }
}

@Composable
private fun MonthExpensesSummary(month: Month, spend: Double) {
    Card(
        modifier = Modifier
            .fillMaxHeight(0.2F)
            .fillMaxWidth(0.9F),
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "This month (${month.shortName})",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = spend.usd, style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun ExpensesList(expensesList: List<Expense>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(expensesList) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RectangleShape) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(text = it.summary, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = it.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Text(
                        text = it.amount.usd, style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeWithExpensesListPreview() {
    HomeWithExpensesList(
        uiState = HomeScreenState(
            expensesList = listOf(
                Expense(
                    id = UUID.randomUUID().toString(),
                    summary = "Groceries",
                    amount = 76.0,
                    createdAt = ZonedDateTime.now()
                ), Expense(
                    id = UUID.randomUUID().toString(),
                    summary = "Movie tickets",
                    amount = 18.0,
                    createdAt = ZonedDateTime.now()
                )
            ),
            thisMonth = Month.NOVEMBER,
            error = null,
        )
    )
}

@Preview
@Composable
private fun HomeWithoutExpensesListPreview() {
    HomeWithExpensesList(
        uiState = HomeScreenState(
            expensesList = emptyList(),
            thisMonth = Month.NOVEMBER,
            error = null,
        )
    )
}

