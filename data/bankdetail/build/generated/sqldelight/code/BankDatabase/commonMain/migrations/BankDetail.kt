package migrations

import app.cash.sqldelight.ColumnAdapter
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String

public data class BankDetail(
  public val bankId: String,
  public val bankName: String,
  public val city: String,
  public val state: String,
  public val centre: String,
  public val swiftCode: String,
  public val branch: String,
  public val district: String,
  public val contact: String,
  public val address: String,
  public val bankCode: String,
  public val IFSCCode: String,
  public val ISO3166: String,
  public val MICR: String,
  public val priority: Int,
  public val isSupportUPI: Boolean,
  public val isSupportNEFT: Boolean,
  public val isSupportRTGS: Boolean,
  public val isSupportIMPS: Boolean,
  public val isEnabled: Boolean,
) {
  public class Adapter(
    public val priorityAdapter: ColumnAdapter<Int, Long>,
  )
}
