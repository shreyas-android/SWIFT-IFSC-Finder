package com.jackson.android.bank.detail.ui.uistate

import com.jackson.android.bank.detail.data.enums.ScreenType
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.domain.bankdetail.data.model.BankInfo

data class BankUIState(
        val bankSearchQuery:String,
        val bankList:List<BankInfo>,
        val openDrawer:Boolean,
        val bankDetailSearchQuery:String,
        val isSearchActive:Boolean,
        val selectedFilterTypeSet:Set<Int>,
        val selectedScreenType: ScreenType,
        val bankFilterInfo : BankFilterInfo){
    companion object{
        fun getDefault() = BankUIState("",listOf(),  false, "", false, setOf(),
            ScreenType.IFSC,
            BankFilterInfo(),)
    }
}