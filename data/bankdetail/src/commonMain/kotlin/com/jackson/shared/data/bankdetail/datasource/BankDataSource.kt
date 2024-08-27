package com.jackson.shared.data.bankdetail.datasource

import app.cash.paging.PagingSource
import com.jackson.shared.common.bankdetail.flow.CommonFlow
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo
import migrations.BankDetail
import migrations.BankSwift
import migrations.Banks

interface BankDataSource {

    suspend fun insertBanks(banks:List<Banks>)

    suspend fun insertBankDetail(bankDetailList:List<BankDetail>)

    suspend fun insertBankSwifts(bankSwiftList : List<BankSwift>)

    suspend fun getBankList(): List<Banks>

    suspend fun getBankListFlow(): CommonFlow<List<Banks>>

    suspend fun getBankInfoListByBankId(bankId:String): CommonFlow<List<BankDetail>>

    suspend fun getBankInfoList(): CommonFlow<List<BankDetail>>

    suspend fun getBankSwiftCodeList(swiftCodeFilterInfo : SwiftCodeFilterInfo):List<BankSwift>

    fun getBankDetailsPagingSource() : PagingSource<Int, BankDetail>

    fun getBankSwiftCodePagingSource():PagingSource<Int, BankSwift>

    fun getFilteredBankDetailsPagingSource(bankFilterInfo : BankFilterInfo) : PagingSource<Int, BankDetail>

    fun getFilteredBankSwiftPagingSource(
            swiftCodeFilterInfo : SwiftCodeFilterInfo): PagingSource<Int, BankSwift>

    fun getBankInfoPagingSource(query:String) : PagingSource<Int, Banks>

    suspend fun updateBankEnabled(isEnabled:Boolean, id:String)

    suspend fun updateBankDetailEnabledByBankId(isEnabled:Boolean, id:String)

    suspend fun updateBankSwiftCodeByBankIFSCCode(swiftCode:String, ifscCode:String)

    suspend fun updateBankOffset(offset:Long, id:String)

    suspend fun updateBankSwiftFetched(swiftFetched : Boolean, id : String)

    suspend fun updateBankListFetched(listFetched : Boolean, id : String)

    suspend fun getFilteredBankDetails(
            bankName : String, city : String, state : String, district : String, ifscCode : String,
            bankCode : String): CommonFlow<List<BankDetail>>


}