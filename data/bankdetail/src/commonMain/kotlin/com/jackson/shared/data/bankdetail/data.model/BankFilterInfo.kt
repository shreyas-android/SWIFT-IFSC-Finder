package com.jackson.shared.data.bankdetail.data.model

data class BankFilterInfo(val bankName : String = "", val city : String = "", val state : String = "",
                          val district : String = "", val ifscCode : String = "",
                          val branch:String = "", val swiftCode:String = "",
                          val country:String = "",
                          val bankCode : String = ""){

    fun isEmpty(shouldCheckIfscCode:Boolean) = bankName.isEmpty() && city.isEmpty() && state.isEmpty() && district.isEmpty() &&
             bankCode.isEmpty() && branch.isEmpty() && country.isEmpty() && if(shouldCheckIfscCode){
                 ifscCode.isEmpty()
             }else{
                 swiftCode.isEmpty()
             }
}