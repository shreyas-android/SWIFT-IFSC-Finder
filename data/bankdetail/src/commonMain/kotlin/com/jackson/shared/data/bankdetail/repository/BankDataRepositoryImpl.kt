package com.jackson.shared.data.bankdetail.repository


import app.cash.paging.PagingSource
import com.jackson.shared.common.bankdetail.flow.CommonFlow
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo

import com.jackson.shared.data.bankdetail.datasource.BankDataSource
import migrations.BankDetail
import migrations.BankSwift
import migrations.Banks

class BankDataRepositoryImpl(private val dataSource : BankDataSource): BankDataRepository {

    override suspend fun insertBanks(banks : List<Banks>) {
        dataSource.insertBanks(banks)
    }

    override suspend fun insertBankDetail(bankDetailList : List<BankDetail>) {
        dataSource.insertBankDetail(bankDetailList)
    }

    override suspend fun insertBankSwifts(bankSwiftList : List<BankSwift>) {
        dataSource.insertBankSwifts(bankSwiftList)
    }

    override suspend fun getBankList() : List<Banks> {
        return dataSource.getBankList()
    }

    override suspend fun getBanksItemsFlow() : CommonFlow<List<Banks>> {
      return  dataSource.getBankListFlow()
    }

    override suspend fun getBankDetailItemsByBankId(bankId : String) : CommonFlow<List<BankDetail>> {
        return dataSource.getBankInfoListByBankId(bankId)
    }

    override suspend fun getBankDetailInfoList() : CommonFlow<List<BankDetail>> {
        return dataSource.getBankInfoList()
    }

    override fun getBankDetailsPagingSource() : PagingSource<Int, BankDetail>{
        return dataSource.getBankDetailsPagingSource()
    }

    override fun getBankSwiftCodePagingSource() : PagingSource<Int, BankSwift> {
        return dataSource.getBankSwiftCodePagingSource()
    }

    override fun getBankInfoPagingSource(query:String) : PagingSource<Int, Banks> {
        return dataSource.getBankInfoPagingSource(query)
    }

    override fun getFilteredBankDetailsPagingSource(
            bankFilterInfo : BankFilterInfo) : PagingSource<Int, BankDetail> {
        return dataSource.getFilteredBankDetailsPagingSource(bankFilterInfo)
    }

    override fun getFilteredBankSwiftPagingSource(
            swiftCodeFilterInfo : SwiftCodeFilterInfo) : PagingSource<Int, BankSwift> {
        return dataSource.getFilteredBankSwiftPagingSource(swiftCodeFilterInfo)
    }

    override suspend fun updateBankEnabled(isEnabled : Boolean, id : String) {
        return dataSource.updateBankEnabled(isEnabled, id)
    }

    override suspend fun updateBankDetailEnabledByBankId(isEnabled : Boolean, id : String) {
        return dataSource.updateBankDetailEnabledByBankId(isEnabled, id)
    }

    override suspend fun updateBankSwiftCodeByBankIFSCCode(swiftCode : String, ifscCode : String) {
        return dataSource.updateBankSwiftCodeByBankIFSCCode(swiftCode, ifscCode)
    }

    override suspend fun updateBankOffset(offset : Long, id : String) {
        return dataSource.updateBankOffset(offset, id)
    }

    override suspend fun updateBankSwiftFetched(swiftFetched : Boolean, id : String) {
        return dataSource.updateBankSwiftFetched(swiftFetched, id)
    }

    override suspend fun updateBankListFetched(listFetched : Boolean, id : String) {
        return dataSource.updateBankListFetched(listFetched, id)
    }

    override suspend fun getFilteredBankDetails(
            bankName : String, city : String, state : String, district : String, ifscCode : String,
            bankCode : String): CommonFlow<List<BankDetail>> {
        return dataSource.getFilteredBankDetails(bankName, city, state, district, ifscCode, bankCode)
    }



}