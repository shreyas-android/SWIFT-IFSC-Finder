package com.jackson.android.bank.detail.ui.uistate

import com.jackson.android.bank.detail.data.enums.ScreenType
import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo
import com.jackson.shared.domain.bankdetail.data.model.BankInfo

sealed class BankUIEvent {

    data class OnOpenDrawer(val shouldOpen:Boolean):BankUIEvent()

    data class OnSelectAllBankChanged(val isChecked:Boolean):BankUIEvent()
    data class OnBankInfoEnableChanged(val bankInfo : BankInfo, val isEnabled:Boolean):BankUIEvent()

    data class OnSearchQueryChanged(val query:String):BankUIEvent()

    data class OnSearchActiveChanged(val isActive:Boolean):BankUIEvent()

    data class OnFilterSelected(val filterType:Int, val enabled:Boolean):BankUIEvent()

    data class OnRemoveFilter(val filterType:Int):BankUIEvent()

    data class OnFilterValueChanged(val filterType : Int, val value:String):BankUIEvent()

    data object OnClearAllFilter: BankUIEvent()

    data object OnKeyBackPress:BankUIEvent()

    data class OnBankDetailClicked(val bankDetailInfo : BankDetailInfo):BankUIEvent()

    data class OnGetSwiftCodeClicked(val bankDetailInfo : BankDetailInfo):BankUIEvent()

    data class OnSwiftFilterSelected(val filterType:Int, val enabled:Boolean):BankUIEvent()

    data class OnSwiftFilterValueChanged(val filterType : Int, val value:String):BankUIEvent()
    data class OnSwiftRemoveFilter(val filterType:Int):BankUIEvent()

    data object OnSwiftClearAllFilter: BankUIEvent()

    data object OnSwiftKeyBackPress:BankUIEvent()

    data class OnBankSearchQueryChanged(val query:String):BankUIEvent()


    data class OnScreenTypeChanged(val screenType : ScreenType): BankUIEvent()
}