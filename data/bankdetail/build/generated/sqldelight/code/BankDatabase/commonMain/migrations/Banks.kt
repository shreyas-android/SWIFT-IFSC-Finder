package migrations

import app.cash.sqldelight.ColumnAdapter
import kotlin.Boolean
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String

public data class Banks(
  public val id: String,
  public val bankName: String,
  public val bankType: String?,
  public val bankCode: String,
  public val priority: Int,
  public val minSavingsInterest: Float?,
  public val maxSavingsInterest: Float?,
  public val isEnabled: Boolean,
  public val offset: Long,
  public val isSwiftCodeFetched: Boolean,
  public val isListFetched: Boolean,
) {
  public class Adapter(
    public val priorityAdapter: ColumnAdapter<Int, Long>,
    public val minSavingsInterestAdapter: ColumnAdapter<Float, Double>,
    public val maxSavingsInterestAdapter: ColumnAdapter<Float, Double>,
  )
}
