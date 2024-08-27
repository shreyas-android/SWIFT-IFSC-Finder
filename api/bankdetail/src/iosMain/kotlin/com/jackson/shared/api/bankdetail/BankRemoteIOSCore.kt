package com.jackson.shared.api.bankdetail

import com.jackson.shared.common.bankdetail.jsonreader.IOSSharedFileReader

object BankRemoteIOSCore {

    fun init(){
        val sharedFileReader = IOSSharedFileReader()
        BankRemoteCore.init(sharedFileReader)
    }
}