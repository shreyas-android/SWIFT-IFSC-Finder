package com.jackson.shared.data.bankdetail.data.model

data class SwiftCodeFilterInfo(val bankName:String = "", val city:String = "",
                               val branch:String = "", val country:String = "", val swiftCode:String = ""){
    fun isEmpty() = bankName.isEmpty() && city.isEmpty() && country.isEmpty() && branch.isEmpty() && swiftCode.isEmpty()
}