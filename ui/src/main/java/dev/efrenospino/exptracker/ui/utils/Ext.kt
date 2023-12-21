package dev.efrenospino.exptracker.ui.utils

import java.time.Month

fun previousMonth(month: Month, year: Int): Pair<Month, Int> {
    if (month == Month.JANUARY) {
        return Pair(Month.DECEMBER, year - 1)
    }
    return Pair(Month.of(month.value - 1), year)
}

fun nextMonth(month: Month, year: Int): Pair<Month, Int> {
    if (month == Month.DECEMBER) {
        return Pair(Month.JANUARY, year + 1)
    }
    return Pair(Month.of(month.value + 1), year)
}
