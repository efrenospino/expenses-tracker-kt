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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.efrenospino.domain.usecases.SortExpensesList
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.ui.R
import dev.efrenospino.ui.lib.ExpensesSortByDropdownMenu
import dev.efrenospino.ui.lib.MonthYearBottomSheetPicker
import dev.efrenospino.ui.lib.simpleHomeTopAppBar
import dev.efrenospino.ui.lib.singleActionBottomBar
import dev.efrenospino.ui.nav.NavigationEffect
import dev.efrenospino.ui.utils.fullName
import dev.efrenospino.ui.utils.shortName
import dev.efrenospino.ui.utils.usd
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

    ExpensesHomeScaffold(uiState = uiState, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpensesHomeScaffold(
    uiState: HomeScreenState,
    onEvent: (Event) -> Unit = {},
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(topBar = simpleHomeTopAppBar(screenTitle = "My Expenses", actions = {
        TextButton(
            onClick = {
                showBottomSheet = true
            }
        ) {
            Text(text = uiState.selectedMonth.shortName)
            Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Pick Month")
        }
    }), bottomBar = singleActionBottomBar(buttonText = "Register New Expense", onClick = {
        onEvent(Event.OnRegisterNewExpenseButtonClicked)
    })
    ) { innerPadding ->

        Box(Modifier.padding(innerPadding)) {
            HomeScreenScaffoldContent(uiState, onEvent)
        }

        if (showBottomSheet) {
            MonthYearBottomSheetPicker(
                month = uiState.selectedMonth,
                year = uiState.selectedYear,
                scope = scope,
                sheetState = sheetState,
                onMonthYearSet = { month, year ->
                    onEvent(
                        Event.OnYearMonthSetButtonClicked(
                            month,
                            year
                        )
                    )
                },
                onDismissRequest = { showBottomSheet = false })
        }
    }
}

@Composable
private fun HomeScreenScaffoldContent(
    uiState: HomeScreenState,
    onEvent: (Event) -> Unit = {},
) {
    Column(
        Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpensesSummaryCard(uiState, onEvent)
        Spacer(modifier = Modifier.size(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            SortByButton(uiState.sortBy, onEvent)
            OrderByButton(uiState.orderByDescending, onEvent)
        }
        if (uiState.expensesList.isEmpty()) {
            EmptyState()
        } else {
            ExpensesList(uiState.expensesList, onEvent)
        }
    }
}

@Composable
private fun SortByButton(
    sortBy: SortExpensesList.SortBy,
    onEvent: (Event) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.wrapContentSize()) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(id = R.drawable.outline_filter_list_24),
                contentDescription = "Filter"
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ExpensesSortByDropdownMenu(sortBy) {
                onEvent(Event.OnSortByOptionChanged(it))
                expanded = false
            }
        }
    }
}

@Composable
private fun OrderByButton(orderByDescending: Boolean, onEvent: (Event) -> Unit) {
    IconButton(onClick = {
        onEvent(Event.OnOrderByOptionChanged(!orderByDescending))
    }) {
        val iconToUse =
            if (orderByDescending) R.drawable.baseline_arrow_downward_24 else R.drawable.baseline_arrow_upward_24
        Icon(
            painter = painterResource(id = iconToUse), contentDescription = "Order"
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
        Modifier.fillMaxSize(0.8F),
        verticalArrangement = Arrangement.Center,
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
}

@Composable
private fun ExpensesSummaryCard(uiState: HomeScreenState, onEvent: (Event) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxHeight(0.2F)
            .fillMaxWidth(0.9F),
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            IconButton(onClick = {
                onEvent(Event.OnGoToPreviousMonthButtonClicked)
            }) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = "Prev"
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${uiState.selectedMonth.fullName} ${uiState.selectedYear}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = uiState.selectedMonthSpend.usd,
                    style = MaterialTheme.typography.headlineMedium
                )
                if (uiState.displayGoToCurrentMonth) {
                    TextButton(onClick = { onEvent(Event.OnGoToCurrentMonthButtonClicked) }) {
                        Text(text = "Back to ${uiState.currentMonth.fullName} ${uiState.currentYear}")
                    }
                }
            }

            IconButton(enabled = uiState.displayGoToNextMonthButton, onClick = {
                onEvent(Event.OnGoToNextMonthButtonClicked)
            }) {
                Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = "Next")
            }
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
    ExpensesHomeScaffold(
        uiState = HomeScreenState(
            selectedYear = 2023,
            selectedMonth = Month.NOVEMBER,
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
        )
    )
}

@Preview
@Composable
private fun HomeWithoutExpensesListPreview() {
    ExpensesHomeScaffold(
        uiState = HomeScreenState(
            selectedMonth = Month.DECEMBER,
            expensesList = emptyList(),
        )
    )
}

