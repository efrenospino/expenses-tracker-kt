package dev.efrenospino.exptracker.ui.ext

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

inline val ZonedDateTime?.displayName: String
    get() = if (this == null || this.toLocalDate().equals(LocalDate.now(ZoneId.systemDefault()))) {
        "Today"
    } else {
        format(DateTimeFormatter.ISO_LOCAL_TIME)
    }
