package com.jackson.shared.api.bankdetail.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BankDetailPagingResponse(
        @SerialName("data")
        val bankDetailResponseItems : List<BankDetailResponse>,
        @SerialName("hasNext")
        val hasNext : Boolean,
        @SerialName("count")
        val count : Int)


@Serializable
data class BankDetailResponse(
        @SerialName("BANK")
        val bankName: String,
        @SerialName("IFSC")
        val ifsc: String,
        @SerialName("BRANCH")
        val branch: String,
        @SerialName("CENTRE")
        val centre: String,
        @SerialName("DISTRICT")
        val district: String,
        @SerialName("STATE")
        val state: String,
        @SerialName("ADDRESS")
        val address: String,
        @SerialName("CONTACT")
        val contact: String,
        @SerialName("IMPS")
        val imps: Boolean,
        @SerialName("RTGS")
        val rtgs: Boolean,
        @SerialName("CITY")
        val city: String?,
        @SerialName("ISO3166")
        val iso3166: String?,
        @SerialName("NEFT")
        val neft: Boolean,
        @SerialName("MICR")
        val micr: String? = null,
        @SerialName("UPI")
        val upi: Boolean?,
        @SerialName("SWIFT")
        val swift: String? = null,
        @SerialName("BANKCODE")
        val bankCode: String)