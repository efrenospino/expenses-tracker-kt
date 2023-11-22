package dev.efrenospino.ui.lib

import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

inline val Double.usd: String get() = String.format("\$%.2f USD", this)

inline val Month.shortName: String get() = this.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US)