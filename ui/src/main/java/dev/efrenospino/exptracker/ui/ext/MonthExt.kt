package dev.efrenospino.exptracker.ui.ext

import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

inline val Month.shortName: String
    get() = this.getDisplayName(
        TextStyle.SHORT_STANDALONE,
        Locale.US
    )

inline val Month.fullName: String
    get() = this.getDisplayName(
        TextStyle.FULL_STANDALONE,
        Locale.US
    )
