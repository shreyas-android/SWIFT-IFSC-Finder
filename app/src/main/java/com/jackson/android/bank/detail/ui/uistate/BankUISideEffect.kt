package com.jackson.android.bank.detail.ui.uistate

import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo

sealed class BankUISideEffect {
    data object NavigateSwiftCode : BankUISideEffect()
}