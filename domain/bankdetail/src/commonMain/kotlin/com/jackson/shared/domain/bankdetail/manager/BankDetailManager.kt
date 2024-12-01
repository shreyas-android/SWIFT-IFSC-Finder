package com.jackson.shared.domain.bankdetail.manager

import app.cash.paging.PagingSource
import com.jackson.shared.domain.bankdetail.Instance
import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo
import com.jackson.shared.domain.bankdetail.data.model.BankInfo
import com.jackson.shared.common.bankdetail.flow.CommonFlow
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo
import database.GetBankSwiftByOffset
import database.GetEnabledBankDetailsByOffset
import database.GetFilteredBankDetails
import database.GetFilteredBankSwift
import migrations.BankDetail
import migrations.BankSwift
import migrations.Banks

interface BankDetailManager {

    companion object{
        fun getInstance() = Instance.bankDetailManager
    }

    suspend fun updateBankListFromRemote(onSuccess:()->Unit)

    suspend fun updateBankPagingFromRemote(isRefresh:Boolean, onDataUpdated:()->Unit)

    suspend fun updateBankSwiftFromRemote(apiKey:String, isRefresh:Boolean, onDataUpdated:()->Unit)

    suspend fun getBankInfoItems(): CommonFlow<List<BankInfo>>


    suspend fun updateBankEnabled(isEnabled:Boolean, id:String)

    suspend fun updateAllBankEnabled(isEnabled:Boolean)

    suspend fun isAllBankSelected(): CommonFlow<Boolean>

    suspend fun updateBankOffset(offset:Long, id:String)

    suspend fun updateBankSwiftFetched(swiftFetched : Boolean, id : String)

     suspend fun updateBankListFetched(listFetched : Boolean, id : String)

    fun getBankDetailsPagingSource() : PagingSource<Int, GetEnabledBankDetailsByOffset>

    fun getBankSwiftCodePagingSource():PagingSource<Int, GetBankSwiftByOffset>

    fun getBankInfoPagingSource(query:String) : PagingSource<Int, Banks>

    fun getFilteredBankDetailsPagingSource(
            bankFilterInfo : BankFilterInfo) : PagingSource<Int, GetFilteredBankDetails>

    fun getFilteredBankSwiftPagingSource(
            swiftCodeFilterInfo : SwiftCodeFilterInfo) : PagingSource<Int, GetFilteredBankSwift>


}