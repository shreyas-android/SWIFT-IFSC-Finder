package com.jackson.shared.api.bankdetail

import com.jackson.shared.common.bankdetail.jsonreader.SharedFileReader

internal object BankRemoteCore {

    internal lateinit var instance : Instance
    fun init(sharedFileReader : SharedFileReader){
        instance = Instance(sharedFileReader)
    }
}