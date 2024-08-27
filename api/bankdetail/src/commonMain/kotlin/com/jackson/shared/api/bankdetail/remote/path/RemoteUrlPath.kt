package com.jackson.shared.api.bankdetail.remote.path

internal class RemoteUrlPath() {

    private fun getAPIBaseURL() = ""

    fun getBankList() = "https://jaf1njzrqj.execute-api.eu-north-1.amazonaws.com/production/banks"

   fun getBankSwift() = "https://api.api-ninjas.com/v1/swiftcode"

    fun getBankInfoFromBankCode() = "https://ifsc.razorpay.com/search"

}