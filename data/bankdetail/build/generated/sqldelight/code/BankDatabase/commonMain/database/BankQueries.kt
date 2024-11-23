package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import migrations.Banks

public class BankQueries(
  driver: SqlDriver,
  private val BanksAdapter: Banks.Adapter,
) : TransacterImpl(driver) {
  public fun <T : Any> getBankList(mapper: (
    id: String,
    bankName: String,
    bankType: String?,
    bankCode: String,
    priority: Int,
    minSavingsInterest: Float?,
    maxSavingsInterest: Float?,
    isEnabled: Boolean,
    offset: Long,
    isSwiftCodeFetched: Boolean,
    isListFetched: Boolean,
  ) -> T): Query<T> = Query(54_475_975, arrayOf("Banks"), driver, "bank.sq", "getBankList",
      "SELECT * FROM Banks ORDER BY priority") { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3)!!,
      BanksAdapter.priorityAdapter.decode(cursor.getLong(4)!!),
      cursor.getDouble(5)?.let { BanksAdapter.minSavingsInterestAdapter.decode(it) },
      cursor.getDouble(6)?.let { BanksAdapter.maxSavingsInterestAdapter.decode(it) },
      cursor.getBoolean(7)!!,
      cursor.getLong(8)!!,
      cursor.getBoolean(9)!!,
      cursor.getBoolean(10)!!
    )
  }

  public fun getBankList(): Query<Banks> = getBankList { id, bankName, bankType, bankCode, priority,
      minSavingsInterest, maxSavingsInterest, isEnabled, offset, isSwiftCodeFetched,
      isListFetched ->
    Banks(
      id,
      bankName,
      bankType,
      bankCode,
      priority,
      minSavingsInterest,
      maxSavingsInterest,
      isEnabled,
      offset,
      isSwiftCodeFetched,
      isListFetched
    )
  }

  public fun <T : Any> getBankListByOffset(
    query: String,
    limit: Long,
    offset: Long,
    mapper: (
      id: String,
      bankName: String,
      bankType: String?,
      bankCode: String,
      priority: Int,
      minSavingsInterest: Float?,
      maxSavingsInterest: Float?,
      isEnabled: Boolean,
      offset: Long,
      isSwiftCodeFetched: Boolean,
      isListFetched: Boolean,
    ) -> T,
  ): Query<T> = GetBankListByOffsetQuery(query, limit, offset) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3)!!,
      BanksAdapter.priorityAdapter.decode(cursor.getLong(4)!!),
      cursor.getDouble(5)?.let { BanksAdapter.minSavingsInterestAdapter.decode(it) },
      cursor.getDouble(6)?.let { BanksAdapter.maxSavingsInterestAdapter.decode(it) },
      cursor.getBoolean(7)!!,
      cursor.getLong(8)!!,
      cursor.getBoolean(9)!!,
      cursor.getBoolean(10)!!
    )
  }

  public fun getBankListByOffset(
    query: String,
    limit: Long,
    offset: Long,
  ): Query<Banks> = getBankListByOffset(query, limit, offset) { id, bankName, bankType, bankCode,
      priority, minSavingsInterest, maxSavingsInterest, isEnabled, offset_, isSwiftCodeFetched,
      isListFetched ->
    Banks(
      id,
      bankName,
      bankType,
      bankCode,
      priority,
      minSavingsInterest,
      maxSavingsInterest,
      isEnabled,
      offset_,
      isSwiftCodeFetched,
      isListFetched
    )
  }

  public fun countBanks(): Query<Long> = Query(1_703_172_273, arrayOf("Banks"), driver, "bank.sq",
      "countBanks", "SELECT count(*) FROM Banks WHERE isEnabled = 1") { cursor ->
    cursor.getLong(0)!!
  }

  public fun insertBankList(
    id: String,
    bankName: String,
    bankType: String?,
    bankCode: String,
    priority: Int,
    minSavingsInterest: Float?,
    maxSavingsInterest: Float?,
    isEnabled: Boolean,
    offset: Long,
    isSwiftCodeFetched: Boolean,
  ) {
    driver.execute(-1_002_437_348, """
        |INSERT OR REPLACE INTO Banks (id, bankName, bankType, bankCode, priority, minSavingsInterest, maxSavingsInterest, isEnabled, offset, isSwiftCodeFetched)
        |VALUES (?, ?, ?, ?, ?,?,?,?, ?, ?)
        """.trimMargin(), 10) {
          bindString(0, id)
          bindString(1, bankName)
          bindString(2, bankType)
          bindString(3, bankCode)
          bindLong(4, BanksAdapter.priorityAdapter.encode(priority))
          bindDouble(5, minSavingsInterest?.let { BanksAdapter.minSavingsInterestAdapter.encode(it)
              })
          bindDouble(6, maxSavingsInterest?.let { BanksAdapter.maxSavingsInterestAdapter.encode(it)
              })
          bindBoolean(7, isEnabled)
          bindLong(8, offset)
          bindBoolean(9, isSwiftCodeFetched)
        }
    notifyQueries(-1_002_437_348) { emit ->
      emit("Banks")
    }
  }

  public fun updateBankEnabled(isEnabled: Boolean, id: String) {
    driver.execute(-1_800_397_325, """UPDATE Banks SET isEnabled = ? WHERE id = ?""", 2) {
          bindBoolean(0, isEnabled)
          bindString(1, id)
        }
    notifyQueries(-1_800_397_325) { emit ->
      emit("Banks")
    }
  }

  public fun updateBankOffset(offset: Long, id: String) {
    driver.execute(1_606_464_417, """UPDATE Banks SET offset = ? WHERE id = ?""", 2) {
          bindLong(0, offset)
          bindString(1, id)
        }
    notifyQueries(1_606_464_417) { emit ->
      emit("Banks")
    }
  }

  public fun updateBankSwiftCodeFetched(isSwiftCodeFetched: Boolean, id: String) {
    driver.execute(860_383_143, """UPDATE Banks SET isSwiftCodeFetched = ? WHERE id = ?""", 2) {
          bindBoolean(0, isSwiftCodeFetched)
          bindString(1, id)
        }
    notifyQueries(860_383_143) { emit ->
      emit("Banks")
    }
  }

  public fun updateBankListFetched(isListFetched: Boolean, id: String) {
    driver.execute(-1_860_972_531, """UPDATE Banks SET isListFetched = ? WHERE id = ?""", 2) {
          bindBoolean(0, isListFetched)
          bindString(1, id)
        }
    notifyQueries(-1_860_972_531) { emit ->
      emit("Banks")
    }
  }

  private inner class GetBankListByOffsetQuery<out T : Any>(
    public val query: String,
    public val limit: Long,
    public val offset: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Banks", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Banks", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-185_505_359,
        """SELECT * FROM Banks WHERE bankName LIKE '%' || ? || '%' OR bankCode LIKE '%' || ? || '%' ORDER BY priority LIMIT ? OFFSET ?""",
        mapper, 4) {
      bindString(0, query)
      bindString(1, query)
      bindLong(2, limit)
      bindLong(3, offset)
    }

    override fun toString(): String = "bank.sq:getBankListByOffset"
  }
}
