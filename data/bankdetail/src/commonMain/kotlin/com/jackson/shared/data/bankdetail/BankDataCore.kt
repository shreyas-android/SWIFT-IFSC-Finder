package com.jackson.shared.data.bankdetail

internal object BankDataCore {

    lateinit var instance: Instance
    fun init(databaseDriverFactory: DatabaseDriverFactory){
        instance = Instance(databaseDriverFactory.createDriver())
    }
}