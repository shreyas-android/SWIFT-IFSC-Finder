package migrations

import kotlin.String

public data class BankSwift(
  public val bankId: String,
  public val bankName: String,
  public val city: String,
  public val country: String,
  public val countryCode: String,
  public val branch: String,
  public val swiftCode: String,
)
