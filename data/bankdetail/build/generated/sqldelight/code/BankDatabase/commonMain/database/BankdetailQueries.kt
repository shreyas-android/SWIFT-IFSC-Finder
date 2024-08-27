package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import migrations.BankDetail

public class BankdetailQueries(
  driver: SqlDriver,
  private val BankDetailAdapter: BankDetail.Adapter,
) : TransacterImpl(driver) {
  public fun <T : Any> getBankDetailByBank(bankId: String, mapper: (
    bankId: String,
    bankName: String,
    city: String,
    state: String,
    centre: String,
    swiftCode: String,
    branch: String,
    district: String,
    contact: String,
    address: String,
    bankCode: String,
    IFSCCode: String,
    ISO3166: String,
    MICR: String,
    priority: Int,
    isSupportUPI: Boolean,
    isSupportNEFT: Boolean,
    isSupportRTGS: Boolean,
    isSupportIMPS: Boolean,
    isEnabled: Boolean,
  ) -> T): Query<T> = GetBankDetailByBankQuery(bankId) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!,
      cursor.getString(7)!!,
      cursor.getString(8)!!,
      cursor.getString(9)!!,
      cursor.getString(10)!!,
      cursor.getString(11)!!,
      cursor.getString(12)!!,
      cursor.getString(13)!!,
      BankDetailAdapter.priorityAdapter.decode(cursor.getLong(14)!!),
      cursor.getBoolean(15)!!,
      cursor.getBoolean(16)!!,
      cursor.getBoolean(17)!!,
      cursor.getBoolean(18)!!,
      cursor.getBoolean(19)!!
    )
  }

  public fun getBankDetailByBank(bankId: String): Query<BankDetail> = getBankDetailByBank(bankId) {
      bankId_, bankName, city, state, centre, swiftCode, branch, district, contact, address,
      bankCode, IFSCCode, ISO3166, MICR, priority, isSupportUPI, isSupportNEFT, isSupportRTGS,
      isSupportIMPS, isEnabled ->
    BankDetail(
      bankId_,
      bankName,
      city,
      state,
      centre,
      swiftCode,
      branch,
      district,
      contact,
      address,
      bankCode,
      IFSCCode,
      ISO3166,
      MICR,
      priority,
      isSupportUPI,
      isSupportNEFT,
      isSupportRTGS,
      isSupportIMPS,
      isEnabled
    )
  }

  public fun <T : Any> getEnabledBankDetails(mapper: (
    bankId: String,
    bankName: String,
    city: String,
    state: String,
    centre: String,
    swiftCode: String,
    branch: String,
    district: String,
    contact: String,
    address: String,
    bankCode: String,
    IFSCCode: String,
    ISO3166: String,
    MICR: String,
    priority: Int,
    isSupportUPI: Boolean,
    isSupportNEFT: Boolean,
    isSupportRTGS: Boolean,
    isSupportIMPS: Boolean,
    isEnabled: Boolean,
  ) -> T): Query<T> = Query(-1_103_875_327, arrayOf("BankDetail"), driver, "bankdetail.sq",
      "getEnabledBankDetails", "SELECT * FROM BankDetail WHERE isEnabled = 1") { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!,
      cursor.getString(7)!!,
      cursor.getString(8)!!,
      cursor.getString(9)!!,
      cursor.getString(10)!!,
      cursor.getString(11)!!,
      cursor.getString(12)!!,
      cursor.getString(13)!!,
      BankDetailAdapter.priorityAdapter.decode(cursor.getLong(14)!!),
      cursor.getBoolean(15)!!,
      cursor.getBoolean(16)!!,
      cursor.getBoolean(17)!!,
      cursor.getBoolean(18)!!,
      cursor.getBoolean(19)!!
    )
  }

  public fun getEnabledBankDetails(): Query<BankDetail> = getEnabledBankDetails { bankId, bankName,
      city, state, centre, swiftCode, branch, district, contact, address, bankCode, IFSCCode,
      ISO3166, MICR, priority, isSupportUPI, isSupportNEFT, isSupportRTGS, isSupportIMPS,
      isEnabled ->
    BankDetail(
      bankId,
      bankName,
      city,
      state,
      centre,
      swiftCode,
      branch,
      district,
      contact,
      address,
      bankCode,
      IFSCCode,
      ISO3166,
      MICR,
      priority,
      isSupportUPI,
      isSupportNEFT,
      isSupportRTGS,
      isSupportIMPS,
      isEnabled
    )
  }

  public fun <T : Any> getEnabledBankDetailsByOffset(
    limit: Long,
    offset: Long,
    mapper: (
      bankId: String,
      bankName: String,
      city: String,
      state: String,
      centre: String,
      swiftCode: String,
      branch: String,
      district: String,
      contact: String,
      address: String,
      bankCode: String,
      IFSCCode: String,
      ISO3166: String,
      MICR: String,
      priority: Int,
      isSupportUPI: Boolean,
      isSupportNEFT: Boolean,
      isSupportRTGS: Boolean,
      isSupportIMPS: Boolean,
      isEnabled: Boolean,
    ) -> T,
  ): Query<T> = GetEnabledBankDetailsByOffsetQuery(limit, offset) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!,
      cursor.getString(7)!!,
      cursor.getString(8)!!,
      cursor.getString(9)!!,
      cursor.getString(10)!!,
      cursor.getString(11)!!,
      cursor.getString(12)!!,
      cursor.getString(13)!!,
      BankDetailAdapter.priorityAdapter.decode(cursor.getLong(14)!!),
      cursor.getBoolean(15)!!,
      cursor.getBoolean(16)!!,
      cursor.getBoolean(17)!!,
      cursor.getBoolean(18)!!,
      cursor.getBoolean(19)!!
    )
  }

  public fun getEnabledBankDetailsByOffset(limit: Long, offset: Long): Query<BankDetail> =
      getEnabledBankDetailsByOffset(limit, offset) { bankId, bankName, city, state, centre,
      swiftCode, branch, district, contact, address, bankCode, IFSCCode, ISO3166, MICR, priority,
      isSupportUPI, isSupportNEFT, isSupportRTGS, isSupportIMPS, isEnabled ->
    BankDetail(
      bankId,
      bankName,
      city,
      state,
      centre,
      swiftCode,
      branch,
      district,
      contact,
      address,
      bankCode,
      IFSCCode,
      ISO3166,
      MICR,
      priority,
      isSupportUPI,
      isSupportNEFT,
      isSupportRTGS,
      isSupportIMPS,
      isEnabled
    )
  }

  public fun countBankDetails(): Query<Long> = Query(685_527_409, arrayOf("BankDetail"), driver,
      "bankdetail.sq", "countBankDetails", "SELECT count(*) FROM BankDetail WHERE isEnabled = 1") {
      cursor ->
    cursor.getLong(0)!!
  }

  public fun countFilteredBankDetails(
    bankName: String,
    city: String,
    state: String,
    district: String,
    ifscCode: String,
    bankCode: String,
    branch: String,
  ): Query<Long> = CountFilteredBankDetailsQuery(bankName, city, state, district, ifscCode,
      bankCode, branch) { cursor ->
    cursor.getLong(0)!!
  }

  public fun <T : Any> getFilteredBankDetails(
    bankName: String,
    city: String,
    state: String,
    district: String,
    ifscCode: String,
    bankCode: String,
    branch: String,
    limit: Long,
    offset: Long,
    mapper: (
      bankId: String,
      bankName: String,
      city: String,
      state: String,
      centre: String,
      swiftCode: String,
      branch: String,
      district: String,
      contact: String,
      address: String,
      bankCode: String,
      IFSCCode: String,
      ISO3166: String,
      MICR: String,
      priority: Int,
      isSupportUPI: Boolean,
      isSupportNEFT: Boolean,
      isSupportRTGS: Boolean,
      isSupportIMPS: Boolean,
      isEnabled: Boolean,
    ) -> T,
  ): Query<T> = GetFilteredBankDetailsQuery(bankName, city, state, district, ifscCode, bankCode,
      branch, limit, offset) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!,
      cursor.getString(7)!!,
      cursor.getString(8)!!,
      cursor.getString(9)!!,
      cursor.getString(10)!!,
      cursor.getString(11)!!,
      cursor.getString(12)!!,
      cursor.getString(13)!!,
      BankDetailAdapter.priorityAdapter.decode(cursor.getLong(14)!!),
      cursor.getBoolean(15)!!,
      cursor.getBoolean(16)!!,
      cursor.getBoolean(17)!!,
      cursor.getBoolean(18)!!,
      cursor.getBoolean(19)!!
    )
  }

  public fun getFilteredBankDetails(
    bankName: String,
    city: String,
    state: String,
    district: String,
    ifscCode: String,
    bankCode: String,
    branch: String,
    limit: Long,
    offset: Long,
  ): Query<BankDetail> = getFilteredBankDetails(bankName, city, state, district, ifscCode, bankCode,
      branch, limit, offset) { bankId, bankName_, city_, state_, centre, swiftCode, branch_,
      district_, contact, address, bankCode_, IFSCCode, ISO3166, MICR, priority, isSupportUPI,
      isSupportNEFT, isSupportRTGS, isSupportIMPS, isEnabled ->
    BankDetail(
      bankId,
      bankName_,
      city_,
      state_,
      centre,
      swiftCode,
      branch_,
      district_,
      contact,
      address,
      bankCode_,
      IFSCCode,
      ISO3166,
      MICR,
      priority,
      isSupportUPI,
      isSupportNEFT,
      isSupportRTGS,
      isSupportIMPS,
      isEnabled
    )
  }

  public fun insertBankDetail(
    bankId: String,
    bankName: String,
    city: String,
    state: String,
    centre: String,
    swiftCode: String,
    branch: String,
    district: String,
    contact: String,
    address: String,
    bankCode: String,
    IFSCCode: String,
    ISO3166: String,
    MICR: String,
    priority: Int,
    isSupportUPI: Boolean,
    isSupportNEFT: Boolean,
    isSupportRTGS: Boolean,
    isSupportIMPS: Boolean,
    isEnabled: Boolean,
  ) {
    driver.execute(-392_537_376, """
        |INSERT OR REPLACE INTO BankDetail(
        |                                       bankId ,
        |                                       bankName,
        |                                       city ,
        |                                       state ,
        |                                       centre ,
        |                                       swiftCode ,
        |                                       branch ,
        |                                       district ,
        |                                       contact ,
        |                                       address ,
        |                                       bankCode,
        |                                       IFSCCode,
        |                                       ISO3166,
        |                                       MICR ,
        |                                       priority,
        |                                       isSupportUPI,
        |                                       isSupportNEFT,
        |                                       isSupportRTGS,
        |                                       isSupportIMPS, isEnabled)
        |VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)
        """.trimMargin(), 20) {
          bindString(0, bankId)
          bindString(1, bankName)
          bindString(2, city)
          bindString(3, state)
          bindString(4, centre)
          bindString(5, swiftCode)
          bindString(6, branch)
          bindString(7, district)
          bindString(8, contact)
          bindString(9, address)
          bindString(10, bankCode)
          bindString(11, IFSCCode)
          bindString(12, ISO3166)
          bindString(13, MICR)
          bindLong(14, BankDetailAdapter.priorityAdapter.encode(priority))
          bindBoolean(15, isSupportUPI)
          bindBoolean(16, isSupportNEFT)
          bindBoolean(17, isSupportRTGS)
          bindBoolean(18, isSupportIMPS)
          bindBoolean(19, isEnabled)
        }
    notifyQueries(-392_537_376) { emit ->
      emit("BankDetail")
    }
  }

  public fun updateBankEnabled(isEnabled: Boolean, bankId: String) {
    driver.execute(-1_756_579_198, """UPDATE BankDetail SET isEnabled = ? WHERE bankId = ?""", 2) {
          bindBoolean(0, isEnabled)
          bindString(1, bankId)
        }
    notifyQueries(-1_756_579_198) { emit ->
      emit("BankDetail")
    }
  }

  public fun updateBankSwiftCode(swiftCode: String, IFSCCode: String) {
    driver.execute(1_746_370_977, """UPDATE BankDetail SET swiftCode = ? WHERE IFSCCode = ?""", 2) {
          bindString(0, swiftCode)
          bindString(1, IFSCCode)
        }
    notifyQueries(1_746_370_977) { emit ->
      emit("BankDetail")
    }
  }

  private inner class GetBankDetailByBankQuery<out T : Any>(
    public val bankId: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("BankDetail", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("BankDetail", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(861_359_836, """SELECT * FROM BankDetail WHERE bankId = ?""", mapper, 1)
        {
      bindString(0, bankId)
    }

    override fun toString(): String = "bankdetail.sq:getBankDetailByBank"
  }

  private inner class GetEnabledBankDetailsByOffsetQuery<out T : Any>(
    public val limit: Long,
    public val offset: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("BankDetail", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("BankDetail", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-703_494_677,
        """SELECT * FROM BankDetail WHERE isEnabled = 1 ORDER BY priority LIMIT ? OFFSET ?""",
        mapper, 2) {
      bindLong(0, limit)
      bindLong(1, offset)
    }

    override fun toString(): String = "bankdetail.sq:getEnabledBankDetailsByOffset"
  }

  private inner class CountFilteredBankDetailsQuery<out T : Any>(
    public val bankName: String,
    public val city: String,
    public val state: String,
    public val district: String,
    public val ifscCode: String,
    public val bankCode: String,
    public val branch: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("BankDetail", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("BankDetail", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_862_041_638, """
    |SELECT count(*) FROM BankDetail WHERE isEnabled = 1 AND bankName LIKE '%' || ? || '%' AND city LIKE '%' || ? || '%'
    | AND state LIKE '%' || ? || '%' AND district LIKE '%' || ? || '%' AND IFSCCode LIKE '%' || ? || '%'
    | AND bankCode LIKE '%' || ? || '%' AND branch LIKE '%' || ? || '%'
    """.trimMargin(), mapper, 7) {
      bindString(0, bankName)
      bindString(1, city)
      bindString(2, state)
      bindString(3, district)
      bindString(4, ifscCode)
      bindString(5, bankCode)
      bindString(6, branch)
    }

    override fun toString(): String = "bankdetail.sq:countFilteredBankDetails"
  }

  private inner class GetFilteredBankDetailsQuery<out T : Any>(
    public val bankName: String,
    public val city: String,
    public val state: String,
    public val district: String,
    public val ifscCode: String,
    public val bankCode: String,
    public val branch: String,
    public val limit: Long,
    public val offset: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("BankDetail", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("BankDetail", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-2_139_089_741, """
    |SELECT * FROM BankDetail WHERE isEnabled = 1 AND bankName LIKE '%' || ? || '%' AND city LIKE '%' || ? || '%'
    | AND state LIKE '%' || ? || '%' AND district LIKE '%' || ? || '%' AND IFSCCode LIKE '%' || ? || '%'
    | AND bankCode LIKE '%' || ? || '%' AND branch LIKE '%' || ? || '%' ORDER BY priority  LIMIT ? OFFSET ?
    """.trimMargin(), mapper, 9) {
      bindString(0, bankName)
      bindString(1, city)
      bindString(2, state)
      bindString(3, district)
      bindString(4, ifscCode)
      bindString(5, bankCode)
      bindString(6, branch)
      bindLong(7, limit)
      bindLong(8, offset)
    }

    override fun toString(): String = "bankdetail.sq:getFilteredBankDetails"
  }
}
