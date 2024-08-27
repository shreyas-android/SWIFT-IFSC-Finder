package com.jackson.shared.data.bankdetail

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.jackson.shared.data.bankdetail.database.BankDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(schema = BankDatabase.Schema, context =  context, name = "android_bank.db")
    }
}