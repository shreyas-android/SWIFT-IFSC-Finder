package com.jackson.shared.api.bankdetail.repository


import com.jackson.shared.api.bankdetail.BanksResponseRaw
import com.jackson.shared.api.bankdetail.data.response.BankDetailPagingResponse
import com.jackson.shared.api.bankdetail.data.response.BankSwiftResponse
import com.jackson.shared.api.bankdetail.data.response.BanksResponse
import com.jackson.shared.api.bankdetail.remote.endpoints.BankRemoteService
import com.jackson.shared.common.bankdetail.RemoteResponse
import com.jackson.shared.common.bankdetail.jsonreader.SharedFileReader
import com.jackson.shared.common.bankdetail.remoteSafeCall
import io.ktor.client.call.body
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class BankRemoteRepositoryImpl(private val bankRemoteService : BankRemoteService,
                               private val sharedFileReader: SharedFileReader): BankRemoteRepository {

    override suspend fun getBankList() : RemoteResponse<BanksResponse> {
        val jsonString = BanksResponseRaw.rawJson
       return RemoteResponse.Success(Json.decodeFromString<BanksResponse>(jsonString))
    }

    override suspend fun getBankSwiftList(
            apiKey:String,
            bankName : String) : RemoteResponse<List<BankSwiftResponse>> {
        return remoteSafeCall(action = {
            bankRemoteService.getBankSwiftByBankName(apiKey, bankName)
        }, onSuccess = {
            it.body()
        })
    }

    override suspend fun getBankPagingFromBankCode(
            bankCode : String, offset : Long) : RemoteResponse<BankDetailPagingResponse> {
        return remoteSafeCall(action = {
            bankRemoteService.getBankDetailFromBankCode(bankCode, offset)
        }, onSuccess = {
            it.body()
        })
    }

}