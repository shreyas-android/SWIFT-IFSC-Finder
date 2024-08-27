package com.jackson.shared.data.bankdetail

import android.content.Context
 object BankDataAndroidCore {

    fun init(context:Context){
        val databaseDriverFactory = DatabaseDriverFactory(context)
        BankDataCore.init(databaseDriverFactory)
    }
}