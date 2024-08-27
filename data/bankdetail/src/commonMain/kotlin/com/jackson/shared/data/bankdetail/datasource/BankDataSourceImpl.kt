package com.jackson.shared.data.bankdetail.datasource

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jackson.shared.common.bankdetail.flow.CommonFlow
import com.jackson.shared.common.bankdetail.flow.toCommonFlow
import database.BankQueries
import database.BankdetailQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import migrations.BankDetail
import migrations.Banks
import app.cash.sqldelight.paging3.QueryPagingSource
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo
import database.BankswiftQueries
import migrations.BankSwift

class BankDataSourceImpl(
        private val dispatcher : CoroutineDispatcher, private val bankQueries : BankQueries,
        private val bankDetailQueries : BankdetailQueries,
        private val bankSwiftQueries : BankswiftQueries) : BankDataSource {

    override suspend fun insertBanks(banks : List<Banks>) {
        banks.forEach {
            bankQueries.insertBankList(
                it.id, it.bankName, it.bankType, it.bankCode, it.priority, it.minSavingsInterest,
                it.maxSavingsInterest, it.isEnabled, it.offset, it.isSwiftCodeFetched)
        }
    }

    override suspend fun insertBankDetail(bankDetailList : List<BankDetail>) {
        bankDetailList.forEach {
            bankDetailQueries.insertBankDetail(
                bankId = it.bankId, bankName = it.bankName, bankCode = it.bankCode, city = it.city,
                centre = it.centre, contact = it.contact, swiftCode = it.swiftCode,
                state = it.state, isSupportUPI = it.isSupportUPI, isSupportIMPS = it.isSupportIMPS,
                isSupportNEFT = it.isSupportNEFT, isSupportRTGS = it.isSupportRTGS,
                address = it.address, branch = it.branch, district = it.district,
                ISO3166 = it.ISO3166, IFSCCode = it.IFSCCode, MICR = it.MICR,
                priority = it.priority, isEnabled = it.isEnabled)
        }
    }

    override suspend fun insertBankSwifts(bankSwiftList : List<BankSwift>) {
        bankSwiftList.forEach {
            bankSwiftQueries.insertBankSwift(
                it.bankId, it.bankName, it.city, it.country, it.countryCode, it.branch,
                it.swiftCode)
        }
    }

    override suspend fun getBankList() : List<Banks> {
        return bankQueries.getBankList().executeAsList()
    }

    override suspend fun getBankListFlow() : CommonFlow<List<Banks>> {
        return bankQueries.getBankList().asFlow().mapToList(dispatcher).toCommonFlow()
    }

    override suspend fun getBankInfoListByBankId(bankId : String) : CommonFlow<List<BankDetail>> {
        return bankDetailQueries.getBankDetailByBank(bankId).asFlow().mapToList(dispatcher)
            .toCommonFlow()
    }

    override suspend fun getBankInfoList() : CommonFlow<List<BankDetail>> {
        return bankDetailQueries.getEnabledBankDetails().asFlow().mapToList(dispatcher)
            .toCommonFlow()
    }

    override suspend fun getBankSwiftCodeList(
            swiftCodeFilterInfo : SwiftCodeFilterInfo) : List<BankSwift> {
        return bankSwiftQueries.getSwiftCodeForBank(
            swiftCodeFilterInfo.bankName, swiftCodeFilterInfo.city.lowercase(),
            swiftCodeFilterInfo.branch.lowercase(), swiftCodeFilterInfo.country.lowercase())
            .executeAsList()
    }

    override fun getBankInfoPagingSource(query : String) : PagingSource<Int, Banks> {
        return QueryPagingSource(countQuery = bankQueries.countBanks(), transacter = bankQueries,
            context = Dispatchers.IO, queryProvider = { limit, offset ->
                bankQueries.getBankListByOffset(query, limit, offset)
            })
    }

    override fun getBankDetailsPagingSource() : PagingSource<Int, BankDetail> {
        return QueryPagingSource(countQuery = bankDetailQueries.countBankDetails(),
            transacter = bankDetailQueries, context = Dispatchers.IO,
            queryProvider = { limit, offset ->
                bankDetailQueries.getEnabledBankDetailsByOffset(limit, offset)
            })
    }

    override fun getBankSwiftCodePagingSource() : PagingSource<Int, BankSwift> {
        return QueryPagingSource(countQuery = bankSwiftQueries.countBankSwift(),
            transacter = bankSwiftQueries, context = Dispatchers.IO,
            queryProvider = { limit, offset ->
                bankSwiftQueries.getBankSwiftByOffset(limit, offset)
            })
    }

    override fun getFilteredBankDetailsPagingSource(
            bankFilterInfo : BankFilterInfo) : PagingSource<Int, BankDetail> {
        return QueryPagingSource(countQuery = bankDetailQueries.countFilteredBankDetails(
            bankFilterInfo.bankName, bankCode = bankFilterInfo.bankCode, city = bankFilterInfo.city,
            ifscCode = bankFilterInfo.ifscCode, district = bankFilterInfo.district,
            state = bankFilterInfo.state, branch = bankFilterInfo.branch),
            transacter = bankDetailQueries, context = Dispatchers.IO,
            queryProvider = { limit, offset ->
                bankDetailQueries.getFilteredBankDetails(
                    bankFilterInfo.bankName, bankCode = bankFilterInfo.bankCode,
                    city = bankFilterInfo.city, ifscCode = bankFilterInfo.ifscCode,
                    district = bankFilterInfo.district, limit = limit, offset = offset,
                    state = bankFilterInfo.state, branch = bankFilterInfo.branch)
            })
    }

    override fun getFilteredBankSwiftPagingSource(
            swiftCodeFilterInfo : SwiftCodeFilterInfo) : PagingSource<Int, BankSwift> {
        return QueryPagingSource(countQuery = bankSwiftQueries.countFilteredBankDetails(
            swiftCodeFilterInfo.bankName, city = swiftCodeFilterInfo.city,
            branch = swiftCodeFilterInfo.branch, country = swiftCodeFilterInfo.country, swiftcode = swiftCodeFilterInfo.swiftCode),
            transacter = bankDetailQueries, context = Dispatchers.IO,
            queryProvider = { limit, offset ->
                bankSwiftQueries.getFilteredBankSwift(
                    swiftCodeFilterInfo.bankName, city = swiftCodeFilterInfo.city,
                    branch = swiftCodeFilterInfo.branch, country = swiftCodeFilterInfo.country,
                    swiftcode = swiftCodeFilterInfo.swiftCode,
                    limit, offset)
            })
    }

    override suspend fun updateBankEnabled(isEnabled : Boolean, id : String) {
        return bankQueries.updateBankEnabled(isEnabled, id)
    }

    override suspend fun updateBankDetailEnabledByBankId(isEnabled : Boolean, id : String) {
        return bankDetailQueries.updateBankEnabled(isEnabled, id)
    }

    override suspend fun updateBankSwiftCodeByBankIFSCCode(swiftCode : String, ifscCode : String) {
        return bankDetailQueries.updateBankSwiftCode(swiftCode, ifscCode)
    }

    override suspend fun updateBankOffset(offset : Long, id : String) {
        return bankQueries.updateBankOffset(offset, id)
    }

    override suspend fun updateBankSwiftFetched(swiftFetched : Boolean, id : String) {
        return bankQueries.updateBankSwiftCodeFetched(swiftFetched, id)
    }

    override suspend fun updateBankListFetched(listFetched : Boolean, id : String) {
        return bankQueries.updateBankListFetched(listFetched, id)
    }

    override suspend fun getFilteredBankDetails(
            bankName : String, city : String, state : String, district : String, ifscCode : String,
            bankCode : String) : CommonFlow<List<BankDetail>> {
        return bankDetailQueries
            .getFilteredBankDetails(bankName, city, state, district, ifscCode, bankCode, "", 100, 0)
            .asFlow().mapToList(dispatcher).toCommonFlow()
    }

}