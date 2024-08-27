package com.jackson.shared.domain.bankdetail

import android.content.Context
import com.jackson.shared.api.bankdetail.BankRemoteAndroidCore
import com.jackson.shared.data.bankdetail.BankDataAndroidCore

object BankDomainAndroidCore {

    fun init(context: Context){
        BankRemoteAndroidCore.init(context)
        BankDataAndroidCore.init(context)
    }
}