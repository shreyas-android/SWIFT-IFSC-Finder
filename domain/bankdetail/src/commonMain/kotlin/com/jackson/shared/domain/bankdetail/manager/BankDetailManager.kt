package com.jackson.shared.domain.bankdetail.manager

import app.cash.paging.PagingSource
import com.jackson.shared.domain.bankdetail.Instance
import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo
import com.jackson.shared.domain.bankdetail.data.model.BankInfo
import com.jackson.shared.common.bankdetail.flow.CommonFlow
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo
import migrations.BankDetail
import migrations.BankSwift
import migrations.Banks

interface BankDetailManager {

    companion object{
        fun getInstance() = Instance.bankDetailManager
    }

    suspend fun updateBankListFromRemote(onSuccess:()->Unit)

    suspend fun updateBankPagingFromRemote(isRefresh:Boolean)

    suspend fun updateBankSwiftFromRemote(apiKey:String, isRefresh:Boolean)

    suspend fun getBankInfoItems(): CommonFlow<List<BankInfo>>


    suspend fun getBankDetailInfoItemsMap(): CommonFlow<Map<BankInfo, List<BankDetailInfo>>>

    suspend fun updateBankEnabled(isEnabled:Boolean, id:String)

    suspend fun updateBankOffset(offset:Long, id:String)

    suspend fun updateBankSwiftFetched(swiftFetched : Boolean, id : String)

     suspend fun updateBankListFetched(listFetched : Boolean, id : String)

    fun getBankDetailsPagingSource() : PagingSource<Int, BankDetail>

    fun getBankSwiftCodePagingSource():PagingSource<Int, BankSwift>

    fun getBankInfoPagingSource(query:String) : PagingSource<Int, Banks>

    fun getFilteredBankDetailsPagingSource(
            bankFilterInfo : BankFilterInfo) : PagingSource<Int, BankDetail>

    fun getFilteredBankSwiftPagingSource(
            swiftCodeFilterInfo : SwiftCodeFilterInfo) : PagingSource<Int, BankSwift>

    suspend fun getFilteredBankDetails(bankFilterInfo : BankFilterInfo): CommonFlow<Map<BankInfo, List<BankDetailInfo>>>

}