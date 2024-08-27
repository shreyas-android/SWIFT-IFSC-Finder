package com.jackson.shared.domain.bankdetail

import com.jackson.shared.api.bankdetail.BankRemoteIOSCore
import com.jackson.shared.data.bankdetail.BankDataIOSCore

object BankDomainIOSCore {

    fun init(){
        BankRemoteIOSCore.init()
        BankDataIOSCore.init()
    }
}