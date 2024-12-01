package com.jackson.android.bank.detail.data.model

import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo

sealed class ItemBankData {

    data class Detail(val bankDetailInfo : BankDetailInfo):ItemBankData()

    data class Header(val bankId:String, val bankName : String):ItemBankData()
}