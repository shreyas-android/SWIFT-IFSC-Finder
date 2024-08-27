package com.jackson.android.bank.detail

import android.app.Application
import android.util.Log
import com.jackson.shared.domain.bankdetail.BankDomainAndroidCore

class BankDetailApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        BankDomainAndroidCore.init(this)

    }
}