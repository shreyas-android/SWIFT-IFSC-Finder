package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String
import migrations.BankSwift

public class BankswiftQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getBankSwiftByOffset(
    limit: Long,
    offset: Long,
    mapper: (
      bankId: String,
      bankName: String,
      city: String,
      country: String,
      countryCode: String,
      branch: String,
      swiftCode: String,
    ) -> T,
  ): Query<T> = GetBankSwiftByOffsetQuery(limit, offset) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!
    )
  }

  public fun getBankSwiftByOffset(limit: Long, offset: Long): Query<BankSwift> =
      getBankSwiftByOffset(limit, offset) { bankId, bankName, city, country, countryCode, branch,
      swiftCode ->
    BankSwift(
      bankId,
      bankName,
      city,
      country,
      countryCode,
      branch,
      swiftCode
    )
  }

  public fun countBankSwift(): Query<Long> = Query(-1_108_445_394, arrayOf("BankSwift"), driver,
      "bankswift.sq", "countBankSwift", "SELECT count(*) FROM BankSwift") { cursor ->
    cursor.getLong(0)!!
  }

  public fun <T : Any> getSwiftCodeForBank(
    bankName: String,
    city: String,
    branch: String,
    country: String,
    mapper: (
      bankId: String,
      bankName: String,
      city: String,
      country: String,
      countryCode: String,
      branch: String,
      swiftCode: String,
    ) -> T,
  ): Query<T> = GetSwiftCodeForBankQuery(bankName, city, branch, country) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!
    )
  }

  public fun getSwiftCodeForBank(
    bankName: String,
    city: String,
    branch: String,
    country: String,
  ): Query<BankSwift> = getSwiftCodeForBank(bankName, city, branch, country) { bankId, bankName_,
      city_, country_, countryCode, branch_, swiftCode ->
    BankSwift(
      bankId,
      bankName_,
      city_,
      country_,
      countryCode,
      branch_,
      swiftCode
    )
  }

  public fun <T : Any> getFilteredBankSwift(
    bankName: String,
    city: String,
    branch: String,
    country: String,
    swiftcode: String,
    limit: Long,
    offset: Long,
    mapper: (
      bankId: String,
      bankName: String,
      city: String,
      country: String,
      countryCode: String,
      branch: String,
      swiftCode: String,
    ) -> T,
  ): Query<T> = GetFilteredBankSwiftQuery(bankName, city, branch, country, swiftcode, limit,
      offset) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!
    )
  }

  public fun getFilteredBankSwift(
    bankName: String,
    city: String,
    branch: String,
    country: String,
    swiftcode: String,
    limit: Long,
    offset: Long,
  ): Query<BankSwift> = getFilteredBankSwift(bankName, city, branch, country, swiftcode, limit,
      offset) { bankId, bankName_, city_, country_, countryCode, branch_, swiftCode ->
    BankSwift(
      bankId,
      bankName_,
      city_,
      country_,
      countryCode,
      branch_,
      swiftCode
    )
  }

  public fun countFilteredBankDetails(
    bankName: String,
    city: String,
    branch: String,
    country: String,
    swiftcode: String,
  ): Query<Long> = CountFilteredBankDetailsQuery(bankName, city, branch, country, swiftcode) {
      cursor ->
    cursor.getLong(0)!!
  }

  public fun insertBankSwift(
    bankId: String,
    bankName: String,
    city: String,
    country: String,
    countryCode: String,
    branch: String,
    swiftCode: String,
  ) {
    driver.execute(778_721_528, """
        |INSERT OR REPLACE INTO BankSwift (bankId, bankName, city, country, countryCode, branch, swiftCode)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindString(0, bankId)
          bindString(1, bankName)
          bindString(2, city)
          bindString(3, country)
          bindString(4, countryCode)
          bindString(5, branch)
          bindString(6, swiftCode)
        }
    notifyQueries(778_721_528) { emit ->
      emit("BankSwift")
    }
  }

  private inner class GetBankSwiftByOffsetQuery<out T : Any>(
    public val limit: Long,
    public val offset: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("BankSwift", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("BankSwift", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(883_613_489, """SELECT * FROM BankSwift LIMIT ? OFFSET ?""", mapper, 2)
        {
      bindLong(0, limit)
      bindLong(1, offset)
    }

    override fun toString(): String = "bankswift.sq:getBankSwiftByOffset"
  }

  private inner class GetSwiftCodeForBankQuery<out T : Any>(
    public val bankName: String,
    public val city: String,
    public val branch: String,
    public val country: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("BankSwift", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("BankSwift", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(365_693_141, """
    |SELECT * FROM BankSwift WHERE bankName LIKE '%' || ? || '%' AND lower(city) LIKE '%' || ? || '%'
    |AND lower(branch) LIKE '%' || ? || '%' AND lower(country) LIKE '%' || ? || '%'
    """.trimMargin(), mapper, 4) {
      bindString(0, bankName)
      bindString(1, city)
      bindString(2, branch)
      bindString(3, country)
    }

    override fun toString(): String = "bankswift.sq:getSwiftCodeForBank"
  }

  private inner class GetFilteredBankSwiftQuery<out T : Any>(
    public val bankName: String,
    public val city: String,
    public val branch: String,
    public val country: String,
    public val swiftcode: String,
    public val limit: Long,
    public val offset: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("BankSwift", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("BankSwift", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_420_504_592, """
    |SELECT * FROM BankSwift WHERE bankName LIKE '%' || ? || '%' AND lower(city) LIKE '%' || ? || '%'
    |AND lower(branch) LIKE '%' || ? || '%' AND lower(country) LIKE '%' || ? || '%'  AND lower(swiftCode) LIKE '%' || ? || '%'  LIMIT ? OFFSET ?
    """.trimMargin(), mapper, 7) {
      bindString(0, bankName)
      bindString(1, city)
      bindString(2, branch)
      bindString(3, country)
      bindString(4, swiftcode)
      bindLong(5, limit)
      bindLong(6, offset)
    }

    override fun toString(): String = "bankswift.sq:getFilteredBankSwift"
  }

  private inner class CountFilteredBankDetailsQuery<out T : Any>(
    public val bankName: String,
    public val city: String,
    public val branch: String,
    public val country: String,
    public val swiftcode: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("BankSwift", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("BankSwift", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-552_824_058, """
    |SELECT count(*) FROM BankSwift WHERE bankName LIKE '%' || ? || '%' AND lower(city) LIKE '%' || ? || '%'
    |                                AND lower(branch) LIKE '%' || ? || '%' AND lower(country) LIKE '%' || ? || '%'
    |                                AND lower(swiftCode) LIKE '%' || ? || '%'
    """.trimMargin(), mapper, 5) {
      bindString(0, bankName)
      bindString(1, city)
      bindString(2, branch)
      bindString(3, country)
      bindString(4, swiftcode)
    }

    override fun toString(): String = "bankswift.sq:countFilteredBankDetails"
  }
}
