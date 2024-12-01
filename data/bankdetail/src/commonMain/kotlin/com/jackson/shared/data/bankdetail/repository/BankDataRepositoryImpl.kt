package com.jackson.shared.data.bankdetail.repository


import app.cash.paging.PagingSource
import com.jackson.shared.common.bankdetail.flow.CommonFlow
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo

import com.jackson.shared.data.bankdetail.datasource.BankDataSource
import database.GetBankSwiftByOffset
import database.GetEnabledBankDetailsByOffset
import database.GetFilteredBankDetails
import database.GetFilteredBankSwift
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

    override fun getBankDetailsPagingSource() : PagingSource<Int, GetEnabledBankDetailsByOffset> {
        return dataSource.getBankDetailsPagingSource()
    }

    override fun getBankSwiftCodePagingSource() : PagingSource<Int, GetBankSwiftByOffset> {
        return dataSource.getBankSwiftCodePagingSource()
    }

    override fun getBankInfoPagingSource(query:String) : PagingSource<Int, Banks> {
        return dataSource.getBankInfoPagingSource(query)
    }

    override fun getFilteredBankDetailsPagingSource(
            bankFilterInfo : BankFilterInfo) : PagingSource<Int, GetFilteredBankDetails> {
        return dataSource.getFilteredBankDetailsPagingSource(bankFilterInfo)
    }

    override fun getFilteredBankSwiftPagingSource(
            swiftCodeFilterInfo : SwiftCodeFilterInfo) : PagingSource<Int, GetFilteredBankSwift> {
        return dataSource.getFilteredBankSwiftPagingSource(swiftCodeFilterInfo)
    }

    override suspend fun updateBankEnabled(isEnabled : Boolean, id : String) {
        return dataSource.updateBankEnabled(isEnabled, id)
    }

    override suspend fun updateAllBankEnabled(isEnabled : Boolean) {
        return dataSource.updateAllBankEnabled(isEnabled)
    }

    override suspend fun isAllBankSelected() : CommonFlow<Boolean> {
        return dataSource.isAllBankSelected()
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


}