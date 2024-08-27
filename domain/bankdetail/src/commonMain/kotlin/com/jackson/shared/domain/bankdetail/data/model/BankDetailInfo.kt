package com.jackson.shared.domain.bankdetail.data.model


data class BankDetailInfo(
        val bankId:String,
        val bankName : String, val city : String, val ifscCode:String,
        val swiftCode : String, val branch : String)