package dev.efrenospino.ui.lib

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

inline val Double.usd: String get() = String.format("\$ %.2f USD", this)

inline val Double.fmt: String get() = DecimalFormat("#.##").format(this)

inline val Month.shortName: String
    get() = this.getDisplayName(
        TextStyle.SHORT_STANDALONE,
        Locale.US
    )

inline val ZonedDateTime?.displayName: String
    get() = if (this == null || this.toLocalDate().equals(LocalDate.now(ZoneId.systemDefault()))) {
        "Today"
    } else {
        format(DateTimeFormatter.ISO_LOCAL_TIME)
    }
