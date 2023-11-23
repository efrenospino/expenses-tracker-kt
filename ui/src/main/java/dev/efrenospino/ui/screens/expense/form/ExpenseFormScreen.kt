package dev.efrenospino.ui.screens.expense.form

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.efrenospino.exptracker.data.models.Expense
import dev.efrenospino.ui.lib.BasicTextField
import dev.efrenospino.ui.lib.ErrorDialog
import dev.efrenospino.ui.lib.FullWidthTextField
import dev.efrenospino.ui.lib.Message
import dev.efrenospino.ui.lib.RetryDialog
import dev.efrenospino.ui.lib.displayName
import dev.efrenospino.ui.lib.fmt
import dev.efrenospino.ui.lib.simpleTopAppBar
import dev.efrenospino.ui.lib.singleActionBottomBar
import dev.efrenospino.ui.lib.usd
import dev.efrenospino.ui.nav.NavigationEffect
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormScreen(
    viewModel: ExpenseFormViewModel,
    navController: NavHostController,
    screenTitle: String,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.OnLoadExpenseData)
    }

    when (uiState.message) {
        is Message.Error.ExpenseDataNotLoaded -> {
            RetryDialog(
                onDismissRequest = { viewModel.onEvent(Event.OnResetErrors) },
                onRetry = { viewModel.onEvent(Event.OnLoadExpenseData) },
                onCancel = { viewModel.onEvent(Event.OnResetErrors) },
                dialogTitle = "Oops!",
                dialogText = uiState.message!!.readableError()
            )
        }

        is Message.Error.ExpenseNotSaved -> {
            ErrorDialog(
                onDismissRequest = { viewModel.onEvent(Event.OnResetErrors) },
                onConfirmation = { viewModel.onEvent(Event.OnResetErrors) },
                dialogTitle = "Oops!",
                dialogText = uiState.message!!.readableError()
            )
        }

        is Message.Info.ExpenseSaved -> {
            Toast.makeText(
                LocalContext.current,
                uiState.message!!.readableError(),
                Toast.LENGTH_SHORT
            ).show()
        }

        null -> {}
    }

    NavigationEffect(
        navigationChannel = viewModel.navigationChannel, navHostController = navController
    )

    if (uiState.expense == null) {
        EmptyExpenseForm(
            screenTitle = screenTitle,
            onEvent = viewModel::onEvent,
        )
    } else {
        ExpenseFormWithData(
            expense = uiState.expense!!, screenTitle = screenTitle, onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun EmptyExpenseForm(
    screenTitle: String,
    onEvent: (Event) -> Unit = {},
) {

    var expenseAmount by rememberSaveable { mutableStateOf("") }
    var expenseSummary by rememberSaveable { mutableStateOf("") }
    val expenseDate by rememberSaveable { mutableStateOf(ZonedDateTime.now()) }

    ExpenseFormScaffold(
        screenTitle = screenTitle,
        onEvent = onEvent,
        expenseAmount = expenseAmount,
        onExpenseAmountChange = { expenseAmount = it },
        expenseSummary = expenseSummary,
        onExpenseSummaryChange = { expenseSummary = it },
        expenseDate = expenseDate,
    )
}

@Composable
private fun ExpenseFormWithData(
    screenTitle: String,
    expense: Expense,
    onEvent: (Event) -> Unit = {},
) {

    var expenseAmount by rememberSaveable {
        mutableStateOf(expense.amount.fmt)
    }
    var expenseSummary by rememberSaveable {
        mutableStateOf(expense.summary)
    }
    val expenseDate by rememberSaveable {
        mutableStateOf(expense.createdAt)
    }

    ExpenseFormScaffold(
        screenTitle = screenTitle,
        onEvent = onEvent,
        expenseAmount = expenseAmount,
        onExpenseAmountChange = { expenseAmount = it },
        expenseSummary = expenseSummary,
        onExpenseSummaryChange = { expenseSummary = it },
        expenseDate = expenseDate,
    )

}

@Composable
private fun ExpenseFormScaffold(
    screenTitle: String,
    onEvent: (Event) -> Unit,
    expenseAmount: String,
    onExpenseAmountChange: (String) -> Unit,
    expenseSummary: String,
    onExpenseSummaryChange: (String) -> Unit,
    expenseDate: ZonedDateTime,
) {
    Scaffold(topBar = simpleTopAppBar(screenTitle = screenTitle, onNavigationBackClick = {
        onEvent(Event.OnNavigateBack)
    }), bottomBar = singleActionBottomBar(buttonText = "Save", onClick = {
        onEvent(
            Event.OnSaveButtonClicked(
                expenseAmount, expenseSummary, expenseDate
            )
        )
    })) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ExpenseFormFields(
                expenseAmount = expenseAmount,
                onExpenseAmountChange = onExpenseAmountChange,
                expenseSummary = expenseSummary,
                onExpenseSummaryChange = onExpenseSummaryChange,
                expenseDate = expenseDate,
            )
        }
    }
}

@Composable
private fun ExpenseFormFields(
    expenseAmount: String,
    onExpenseAmountChange: (String) -> Unit,
    expenseSummary: String,
    onExpenseSummaryChange: (String) -> Unit,
    expenseDate: ZonedDateTime?,
) {
    Column(modifier = Modifier.padding(10.dp)) {
        FullWidthTextField(value = expenseAmount, placeholder = 0.0.usd, onValueChange = {
            onExpenseAmountChange(it)
        })
        BasicTextField(
            label = "Summary", value = expenseSummary, onValueChange = onExpenseSummaryChange
        )
        BasicTextField(label = "Date", value = expenseDate.displayName, readOnly = true)
    }
}

@Preview
@Composable
private fun EmptyExpensesForm() {
    EmptyExpenseForm(screenTitle = "New Expense")
}


@Preview
@Composable
private fun ExpensesFormWithData() {
    ExpenseFormWithData(
        screenTitle = "New Expense",
        expense = Expense(
            id = "RandomUUID",
            summary = "Movie tickets",
            amount = 35.99,
            createdAt = ZonedDateTime.now()
        ),
    )
}

