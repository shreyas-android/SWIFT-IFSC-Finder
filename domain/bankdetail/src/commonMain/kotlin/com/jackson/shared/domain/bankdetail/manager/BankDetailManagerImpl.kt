package com.jackson.shared.domain.bankdetail.manager

import app.cash.paging.PagingSource
import com.jackson.shared.api.bankdetail.data.response.BankDetailResponse
import com.jackson.shared.api.bankdetail.data.response.BankSwiftResponse
import com.jackson.shared.common.bankdetail.RemoteResponse
import com.jackson.shared.domain.bankdetail.data.mapper.toBankEntity
import com.jackson.shared.data.bankdetail.repository.BankDataRepository
import com.jackson.shared.domain.bankdetail.data.mapper.toBankInfo
import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo
import com.jackson.shared.domain.bankdetail.data.model.BankInfo
import com.jackson.shared.common.bankdetail.flow.CommonFlow
import com.jackson.shared.common.bankdetail.flow.toCommonFlow
import com.jackson.shared.api.bankdetail.repository.BankRemoteRepository
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo
import com.jackson.shared.domain.bankdetail.data.mapper.toBankDetailEntity
import com.jackson.shared.domain.bankdetail.data.mapper.toBankDetailInfo
import com.jackson.shared.domain.bankdetail.data.mapper.toBankSwiftEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import migrations.BankDetail
import migrations.BankSwift
import migrations.Banks

internal class BankDetailManagerImpl(private val bankRemoteRepository: BankRemoteRepository, private val bankDataRepository : BankDataRepository): BankDetailManager {

    private val managerBackgroundScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun updateBankListFromRemote(onSuccess:()->Unit) {
        when(val banksResponse = bankRemoteRepository.getBankList()){
            is RemoteResponse.Error -> {
            }
            is RemoteResponse.Success -> {
                val bankListResponse = banksResponse.data
                bankDataRepository.insertBanks(bankListResponse.bankListInfoResponses.map {
                    it.toBankEntity()
                })
                onSuccess()
            }
        }
    }

    override suspend fun updateBankSwiftFromRemote(apiKey:String, isRefresh:Boolean, onDataUpdated:()->Unit) {
        val bankList = bankDataRepository.getBankList()
        val results =  bankList.map {banks ->
            managerBackgroundScope.async {
            if(isRefresh || !banks.isSwiftCodeFetched) {

                    val bankResponse = bankRemoteRepository.getBankSwiftList(
                        apiKey, banks.bankName.removeSuffix("Ltd."))

                    when(bankResponse) {
                        is RemoteResponse.Error -> {
                        }

                        is RemoteResponse.Success -> {
                            insertBankSwiftResponse(banks, bankResponse.data)
                            updateBankSwiftFetched(true, banks.id)
                        }
                    }
                }
            }
        }

        results.awaitAll()
        onDataUpdated()
    }

    override suspend fun updateBankPagingFromRemote(isRefresh:Boolean, onDataUpdated:()->Unit) {
         val bankList = bankDataRepository.getBankList()
       val results =  bankList.map {banks ->
            managerBackgroundScope.async {
            if(isRefresh || !banks.isListFetched) {

                    var hasNext = false
                    var count = banks.offset
                    do {
                        val bankDetailPagingResponse =
                            bankRemoteRepository.getBankPagingFromBankCode(
                                banks.bankCode, banks.offset)

                        when(bankDetailPagingResponse) {
                            is RemoteResponse.Error -> {
                                hasNext = false
                            }

                            is RemoteResponse.Success -> {
                                val response = bankDetailPagingResponse.data
                                hasNext = response.hasNext
                                count += response.bankDetailResponseItems.size

                                insertBankDetailResponse(banks, response.bankDetailResponseItems)
                                updateBankOffset(count, banks.id)
                            }
                        }
                    } while(hasNext)

                    if(!hasNext){
                        updateBankListFetched(true, banks.id)
                    }
                }
            }
        }

        results.awaitAll()
        onDataUpdated()
    }

    private suspend fun insertBankDetailResponse(banks : Banks, bankDetailResponseItems : List<BankDetailResponse>){
        bankDataRepository.insertBankDetail(bankDetailResponseItems.map {
            it.toBankDetailEntity(banks.id, banks.bankName, banks.isEnabled, banks.priority)
        })
    }

    private suspend fun insertBankSwiftResponse(banks : Banks, bankSwiftResponseItems : List<BankSwiftResponse>){
        bankDataRepository.insertBankSwifts(bankSwiftResponseItems.map {
            it.toBankSwiftEntity(banks.id, banks.bankName)
        })
    }

    override suspend fun getBankInfoItems() : CommonFlow<List<BankInfo>> {
        return bankDataRepository.getBanksItemsFlow().map {
            it.map { banks ->
                banks.toBankInfo()
            }
        }.toCommonFlow()
    }

    override suspend fun getBankDetailInfoItemsMap() : CommonFlow<Map<BankInfo, List<BankDetailInfo>>> {
      val bankDetailListFlow = bankDataRepository.getBankDetailInfoList()
        val bankListFlow = getBankInfoItems()

       return combine(bankDetailListFlow, bankListFlow){bankDetailList, bankList ->
            val bankListMap = HashMap<String, BankInfo>()
            for(banks in bankList){
                bankListMap[banks.id] = banks
            }

            bankDetailList.groupBy(keySelector = {
                bankListMap[it.bankId]!!
            }, valueTransform = {
                it.toBankDetailInfo()
            })

        }.toCommonFlow()
    }

    override suspend fun updateBankEnabled(isEnabled : Boolean, id : String) {
        bankDataRepository.updateBankEnabled(isEnabled, id)
        bankDataRepository.updateBankDetailEnabledByBankId(isEnabled, id)
    }

    override suspend fun updateBankOffset(offset : Long, id : String) {
        bankDataRepository.updateBankOffset(offset, id)
    }

   override suspend fun updateBankSwiftFetched(swiftFetched : Boolean, id : String){
       bankDataRepository.updateBankSwiftFetched(swiftFetched, id)
   }

    override suspend fun updateBankListFetched(listFetched : Boolean, id : String) {
        bankDataRepository.updateBankListFetched(listFetched, id)
    }

    override suspend fun getFilteredBankDetails(
           bankFilterInfo : BankFilterInfo): CommonFlow<Map<BankInfo, List<BankDetailInfo>>> {
      val filteredBankDetails =   bankDataRepository.getFilteredBankDetails(bankFilterInfo.bankName,
            bankFilterInfo.city, bankFilterInfo.state, bankFilterInfo.district,
            bankFilterInfo.ifscCode, bankFilterInfo.bankCode)

        val bankListFlow = getBankInfoItems()

        return combine(filteredBankDetails, bankListFlow){bankDetailList, bankList ->
            val bankListMap = HashMap<String, BankInfo>()
            for(banks in bankList){
                bankListMap[banks.id] = banks
            }

            bankDetailList.groupBy(keySelector = {
                bankListMap[it.bankId]!!
            }, valueTransform = {
                it.toBankDetailInfo()
            })

        }.toCommonFlow()
    }

   override fun getBankDetailsPagingSource() : PagingSource<Int, BankDetail>{
       return bankDataRepository.getBankDetailsPagingSource()
   }

    override fun getBankSwiftCodePagingSource() : PagingSource<Int, BankSwift> {
        return bankDataRepository.getBankSwiftCodePagingSource()
    }

    override fun getBankInfoPagingSource(query:String) : PagingSource<Int, Banks> {
        return bankDataRepository.getBankInfoPagingSource(query)
    }

    override fun getFilteredBankDetailsPagingSource(
            bankFilterInfo : BankFilterInfo) : PagingSource<Int, BankDetail> {
        return bankDataRepository.getFilteredBankDetailsPagingSource(bankFilterInfo)
    }

    override fun getFilteredBankSwiftPagingSource(
            swiftCodeFilterInfo : SwiftCodeFilterInfo) : PagingSource<Int, BankSwift> {
        return bankDataRepository.getFilteredBankSwiftPagingSource(swiftCodeFilterInfo)
    }

}