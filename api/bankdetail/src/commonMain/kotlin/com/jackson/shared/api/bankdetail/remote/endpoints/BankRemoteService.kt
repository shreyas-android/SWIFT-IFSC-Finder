package com.jackson.shared.api.bankdetail.remote.endpoints

import io.ktor.client.statement.HttpResponse

interface BankRemoteService {

    suspend fun getAllBanks(): HttpResponse

    suspend fun getBankDetailFromBankCode(bankCode:String, offset:Long):HttpResponse

    suspend fun getBankSwiftByBankName(apiKey:String, bankName:String): HttpResponse
}