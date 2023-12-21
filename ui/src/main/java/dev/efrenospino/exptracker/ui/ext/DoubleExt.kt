package dev.efrenospino.exptracker.ui.ext

import java.text.DecimalFormat

inline val Double.usd: String get() = String.format("\$ %.2f USD", this)

inline val Double.fmt: String get() = DecimalFormat("#.##").format(this)
