package com.jackson.shared.api.bankdetail.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BankListInfoResponse(@SerialName("id")
                                val id:String,
                                @SerialName("name")
                                val bankName:String,
                                @SerialName("type")
                                val bankType:String,
                                @SerialName("code")
                                val bankCode:String,
                                @SerialName("priority")
                                val priority:Int,
                                @SerialName("savings_interest_min")
                                val minSavingsInterest:Float? = null,
                                @SerialName("savings_interest_max")
                                val maxSavingsInterest:Float? = null,)

@Serializable
data class BankListResponse(
        @SerialName("banks")
        val bankListInfoResponses:List<BankListInfoResponse>)


@Serializable
data class BanksResponse(
        @SerialName("banks")
        val bankListInfoResponses:List<BankListInfoResponse>)