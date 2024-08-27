package com.jackson.shared.data.bankdetail

object BankDataIOSCore {

    fun init(){
        val databaseDriverFactory = DatabaseDriverFactory()
        BankDataCore.init(databaseDriverFactory)
    }
}