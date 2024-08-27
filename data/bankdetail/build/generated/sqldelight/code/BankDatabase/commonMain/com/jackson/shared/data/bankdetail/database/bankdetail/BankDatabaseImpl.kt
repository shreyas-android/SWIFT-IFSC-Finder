package com.jackson.shared.`data`.bankdetail.database.bankdetail

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.jackson.shared.`data`.bankdetail.database.BankDatabase
import database.BankQueries
import database.BankdetailQueries
import database.BankswiftQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass
import migrations.BankDetail
import migrations.Banks

internal val KClass<BankDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = BankDatabaseImpl.Schema

internal fun KClass<BankDatabase>.newInstance(
  driver: SqlDriver,
  BankDetailAdapter: BankDetail.Adapter,
  BanksAdapter: Banks.Adapter,
): BankDatabase = BankDatabaseImpl(driver, BankDetailAdapter, BanksAdapter)

private class BankDatabaseImpl(
  driver: SqlDriver,
  BankDetailAdapter: BankDetail.Adapter,
  BanksAdapter: Banks.Adapter,
) : TransacterImpl(driver), BankDatabase {
  override val bankQueries: BankQueries = BankQueries(driver, BanksAdapter)

  override val bankdetailQueries: BankdetailQueries = BankdetailQueries(driver, BankDetailAdapter)

  override val bankswiftQueries: BankswiftQueries = BankswiftQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE IF NOT EXISTS Banks (
          |    id TEXT PRIMARY KEY,
          |    bankName TEXT NOT NULL,
          |    bankType TEXT,
          |    bankCode TEXT NOT NULL,
          |    priority INTEGER DEFAULT 0 NOT NULL,
          |    minSavingsInterest REAL,
          |    maxSavingsInterest REAL,
          |    isEnabled  INTEGER DEFAULT 1 NOT NULL,
          |    offset INTEGER DEFAULT 0 NOT NULL,
          |    isSwiftCodeFetched INTEGER DEFAULT 0 NOT NULL,
          |    isListFetched INTEGER DEFAULT 0 NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE IF NOT EXISTS BankDetail (
          |    bankId TEXT NOT NULL,
          |    bankName TEXT NOT NULL,
          |    city TEXT NOT NULL,
          |    state TEXT NOT NULL,
          |    centre TEXT NOT NULL ,
          |    swiftCode TEXT NOT NULL,
          |    branch TEXT NOT NULL,
          |    district TEXT NOT NULL,
          |    contact TEXT NOT NULL,
          |    address TEXT NOT NULL,
          |    bankCode TEXT NOT NULL,
          |    IFSCCode TEXT NOT NULL PRIMARY KEY,
          |    ISO3166 TEXT NOT NULL,
          |    MICR TEXT NOT NULL,
          |     priority INTEGER DEFAULT 0 NOT NULL,
          |    isSupportUPI  INTEGER DEFAULT 0 NOT NULL,
          |    isSupportNEFT INTEGER DEFAULT 0 NOT NULL,
          |    isSupportRTGS  INTEGER DEFAULT 0 NOT NULL,
          |    isSupportIMPS  INTEGER DEFAULT 0 NOT NULL,
          |     isEnabled  INTEGER DEFAULT 1 NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE IF NOT EXISTS BankSwift (
          | bankId TEXT NOT NULL,
          |    bankName TEXT NOT NULL,
          |    city TEXT NOT NULL,
          |    country TEXT NOT NULL,
          |    countryCode TEXT NOT NULL,
          |     branch TEXT NOT NULL,
          |      swiftCode TEXT NOT NULL PRIMARY KEY
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    private fun migrateInternal(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
    ): QueryResult.Value<Unit> {
      if (oldVersion <= 0 && newVersion > 0) {
        driver.execute(null, """
            |CREATE TABLE IF NOT EXISTS Banks (
            |    id TEXT PRIMARY KEY,
            |    bankName TEXT NOT NULL,
            |    bankType TEXT,
            |    bankCode TEXT NOT NULL,
            |    priority INTEGER DEFAULT 0 NOT NULL,
            |    minSavingsInterest REAL,
            |    maxSavingsInterest REAL,
            |    isEnabled  INTEGER DEFAULT 1 NOT NULL,
            |    offset INTEGER DEFAULT 0 NOT NULL,
            |    isSwiftCodeFetched INTEGER DEFAULT 0 NOT NULL,
            |    isListFetched INTEGER DEFAULT 0 NOT NULL
            |)
            """.trimMargin(), 0)
        driver.execute(null, """
            |CREATE TABLE IF NOT EXISTS BankDetail (
            |    bankId TEXT NOT NULL,
            |    bankName TEXT NOT NULL,
            |    city TEXT NOT NULL,
            |    state TEXT NOT NULL,
            |    centre TEXT NOT NULL ,
            |    swiftCode TEXT NOT NULL,
            |    branch TEXT NOT NULL,
            |    district TEXT NOT NULL,
            |    contact TEXT NOT NULL,
            |    address TEXT NOT NULL,
            |    bankCode TEXT NOT NULL,
            |    IFSCCode TEXT NOT NULL PRIMARY KEY,
            |    ISO3166 TEXT NOT NULL,
            |    MICR TEXT NOT NULL,
            |     priority INTEGER DEFAULT 0 NOT NULL,
            |    isSupportUPI  INTEGER DEFAULT 0 NOT NULL,
            |    isSupportNEFT INTEGER DEFAULT 0 NOT NULL,
            |    isSupportRTGS  INTEGER DEFAULT 0 NOT NULL,
            |    isSupportIMPS  INTEGER DEFAULT 0 NOT NULL,
            |     isEnabled  INTEGER DEFAULT 1 NOT NULL
            |)
            """.trimMargin(), 0)
        driver.execute(null, """
            |CREATE TABLE IF NOT EXISTS BankSwift (
            | bankId TEXT NOT NULL,
            |    bankName TEXT NOT NULL,
            |    city TEXT NOT NULL,
            |    country TEXT NOT NULL,
            |    countryCode TEXT NOT NULL,
            |     branch TEXT NOT NULL,
            |      swiftCode TEXT NOT NULL PRIMARY KEY
            |)
            """.trimMargin(), 0)
      }
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> {
      var lastVersion = oldVersion

      callbacks.filter { it.afterVersion in oldVersion until newVersion }
      .sortedBy { it.afterVersion }
      .forEach { callback ->
        migrateInternal(driver, oldVersion = lastVersion, newVersion = callback.afterVersion + 1)
        callback.block(driver)
        lastVersion = callback.afterVersion + 1
      }

      if (lastVersion < newVersion) {
        migrateInternal(driver, lastVersion, newVersion)
      }
      return QueryResult.Unit
    }
  }
}
