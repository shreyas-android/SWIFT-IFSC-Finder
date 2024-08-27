package com.jackson.shared.api.bankdetail.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class  BankSwiftResponse( @SerialName("bank_name")
                               val bankName:String,

                               @SerialName("city")
                               val city:String?,

                               @SerialName("country")
                               val country:String,

                               @SerialName("country_code")
                               val countryCode:String,

                               @SerialName("swift_code")
                               val swiftCode:String,

                               @SerialName("branch")
                               val branch:String? = null)