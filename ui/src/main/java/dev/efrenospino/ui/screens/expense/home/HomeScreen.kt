package dev.efrenospino.ui.screens.expense.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.ui.R
import dev.efrenospino.ui.lib.shortName
import dev.efrenospino.ui.lib.simpleHomeTopAppBar
import dev.efrenospino.ui.lib.singleActionBottomBar
import dev.efrenospino.ui.lib.usd
import dev.efrenospino.ui.nav.NavigationEffect
import java.time.Month
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.OnLoadAllExpenses)
    }

    NavigationEffect(
        navigationChannel = viewModel.navigationChannel, navHostController = navController
    )

    HomeWithExpensesList(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
private fun HomeWithExpensesList(
    uiState: HomeScreenState,
    onEvent: (Event) -> Unit = {},
) {
    Scaffold(
        topBar = simpleHomeTopAppBar(screenTitle = "My Expenses"),
        bottomBar = singleActionBottomBar(buttonText = "Register New Expense", onClick = {
            onEvent(Event.OnRegisterNewExpenseButtonClicked)
        })
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            HomeScreenScaffoldContent(uiState, onEvent)
        }
    }
}

@Composable
private fun HomeScreenScaffoldContent(
    uiState: HomeScreenState,
    onEvent: (Event) -> Unit = {},
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpensesSummaryCard(uiState.thisMonth, uiState.thisMonthSpend)
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
            ExpensesList(uiState.expensesList, onEvent)
        }
    }
}

@Composable
private fun ExpensesSummaryCard(month: Month, spend: Double) {
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
private fun ExpensesList(
    expensesList: List<Expense>,
    onEvent: (Event) -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(expensesList) { expense ->
            ExpenseTile(expense, onEvent)
        }
    }
}

@Composable
private fun ExpenseTile(
    expense: Expense,
    onEvent: (Event) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(Event.OnExpenseRecordClicked(expense))
            }, shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(text = expense.summary, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = expense.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                text = expense.amount.usd, style = MaterialTheme.typography.headlineSmall
            )
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

