package com.jackson.shared.data.bankdetail

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.jackson.shared.data.bankdetail.database.BankDatabase

actual class DatabaseDriverFactory {
  actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(BankDatabase.Schema, "ios_bank.db")
  }
}