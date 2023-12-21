package dev.efrenospino.exptracker.ui.lib

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.efrenospino.exptracker.ui.ext.fullName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Month
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun MonthYearBottomSheetPicker(
    month: Month,
    year: Int,
    scope: CoroutineScope,
    sheetState: SheetState,
    onMonthYearSet: (Month, Int) -> Unit = { _, _ -> },
    onDismissRequest: () -> Unit = {},
) {

    val currentYear = remember { ZonedDateTime.now().year }
    val years = remember { ((currentYear - 5)..currentYear).toList() }
    val months = Month.entries.toTypedArray().asList()

    var selectedMonth by rememberSaveable { mutableStateOf(month) }
    var selectedYear by rememberSaveable { mutableIntStateOf(year) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest, sheetState = sheetState
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                ListPicker(
                    Modifier
                        .fillMaxHeight(0.3F)
                        .fillMaxWidth(0.5F),
                    items = months,
                    selectedItem = selectedMonth,
                    itemDisplayName = { it.fullName },
                    onItemClick = {
                        selectedMonth = it
                    },
                )

                ListPicker(
                    Modifier
                        .fillMaxHeight(0.3F)
                        .fillMaxWidth(0.5F),
                    items = years,
                    selectedItem = selectedYear,
                    itemDisplayName = { it.toString() },
                    onItemClick = {
                        selectedYear = it
                    },
                )
            }

            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onMonthYearSet(selectedMonth, selectedYear)
                        onDismissRequest()
                    }
                }
            }) {
                Text("Set")
            }
        }

    }
}

@Composable
fun <T> ListPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T,
    itemDisplayName: (T) -> String,
    onItemClick: (T) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(modifier) {
        items(items) { item ->
            ListItem(
                modifier = Modifier.clickable { onItemClick(item) },
                headlineContent = {
                    Text(
                        text = itemDisplayName(item),
                        fontWeight = if (selectedItem == item) FontWeight.ExtraBold else FontWeight.Normal
                    )
                },
            )
        }

        coroutineScope.launch {
            listState.animateScrollToItem(items.indexOf(selectedItem))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MonthYearBottomSheetPickerPreview() {
    MonthYearBottomSheetPicker(
        month = Month.NOVEMBER,
        year = 2023,
        scope = CoroutineScope(Dispatchers.Default),
        sheetState = SheetState(initialValue = SheetValue.Expanded, skipPartiallyExpanded = true)
    )
}
