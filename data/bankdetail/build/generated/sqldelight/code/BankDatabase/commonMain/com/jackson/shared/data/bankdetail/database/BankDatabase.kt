package com.jackson.shared.`data`.bankdetail.database

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.jackson.shared.`data`.bankdetail.database.bankdetail.newInstance
import com.jackson.shared.`data`.bankdetail.database.bankdetail.schema
import database.BankQueries
import database.BankdetailQueries
import database.BankswiftQueries
import kotlin.Unit
import migrations.BankDetail
import migrations.Banks

public interface BankDatabase : Transacter {
  public val bankQueries: BankQueries

  public val bankdetailQueries: BankdetailQueries

  public val bankswiftQueries: BankswiftQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = BankDatabase::class.schema

    public operator fun invoke(
      driver: SqlDriver,
      BankDetailAdapter: BankDetail.Adapter,
      BanksAdapter: Banks.Adapter,
    ): BankDatabase = BankDatabase::class.newInstance(driver, BankDetailAdapter, BanksAdapter)
  }
}
