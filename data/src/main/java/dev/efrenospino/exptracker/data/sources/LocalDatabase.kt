package dev.efrenospino.exptracker.data.sources

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.efrenospino.exptracker.data.db.ExpTrackerDB

class LocalDatabase(context: Context) {

    private val dbDriver: SqlDriver =
        AndroidSqliteDriver(ExpTrackerDB.Schema, context, "exptracker.db")
    private val db = ExpTrackerDB(dbDriver)

    val expenseQueries = db.expenseQueries

}
