package com.jackson.shared.api.bankdetail.repository

import com.jackson.shared.api.bankdetail.BankRemoteCore
import com.jackson.shared.api.bankdetail.Instance
import com.jackson.shared.api.bankdetail.data.response.BankDetailPagingResponse
import com.jackson.shared.api.bankdetail.data.response.BankInfoResponse
import com.jackson.shared.api.bankdetail.data.response.BankSwiftResponse
import com.jackson.shared.api.bankdetail.data.response.BanksResponse
import com.jackson.shared.common.bankdetail.RemoteResponse

interface BankRemoteRepository{

    companion object{
        fun getInstance() = BankRemoteCore.instance.bankRepository
    }

    suspend fun getBankList(): RemoteResponse<BanksResponse>

    suspend fun getBankSwiftList(apiKey:String,bankName:String): RemoteResponse<List<BankSwiftResponse>>

    suspend fun getBankPagingFromBankCode(bankCode : String, offset : Long) : RemoteResponse<BankDetailPagingResponse>
}