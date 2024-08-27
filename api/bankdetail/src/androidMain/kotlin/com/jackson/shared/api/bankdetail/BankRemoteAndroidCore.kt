package com.jackson.shared.api.bankdetail

import android.content.Context
import com.jackson.shared.common.bankdetail.jsonreader.AndroidSharedFileReader

object BankRemoteAndroidCore {

    fun init(context: Context){
        val sharedFileReader = AndroidSharedFileReader(context)
        BankRemoteCore.init(sharedFileReader)
    }

}