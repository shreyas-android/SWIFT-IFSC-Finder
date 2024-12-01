package com.jackson.android.bank.detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import app.cash.paging.PagingConfig
import app.cash.paging.Pager
import app.cash.paging.PagingSource
import com.jackson.android.bank.detail.data.enums.ScreenType
import com.jackson.android.bank.detail.data.model.ItemBankData
import com.jackson.android.bank.detail.ui.uistate.BankUIEvent
import com.jackson.android.bank.detail.ui.uistate.BankUISideEffect
import com.jackson.android.bank.detail.ui.uistate.BankUIState
import com.jackson.android.bank.detail.utils.BankProUtils
import com.jackson.android.bank.detail.utils.BankProUtils.checkAndUpdateValue
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo
import com.jackson.shared.domain.bankdetail.data.mapper.toBankDetailInfo
import com.jackson.shared.domain.bankdetail.data.mapper.toBankInfo
import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo
import com.jackson.shared.domain.bankdetail.manager.BankDetailManager
import database.GetBankSwiftByOffset
import database.GetEnabledBankDetailsByOffset
import database.GetFilteredBankDetails
import database.GetFilteredBankSwift
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import migrations.BankDetail
import migrations.BankSwift
import migrations.Banks

class BankDetailViewModel(
        private val apiKey : String,
        private val bankDetailManager : BankDetailManager) : ViewModel() {

    private var shouldRefreshBank : (() -> Unit)? = null

    private var shouldRefreshBankDetail : (() -> Unit)? = null

    private var shouldRefreshFilterBankDetail : (() -> Unit)? = null

    private var shouldRefreshFilterBankSwift : (() -> Unit)? = null

    private var bankSwiftRemoteJob : Job? = null

    private var bankDetailRemoteJob : Job? = null

    private val screenTypeFlow = MutableStateFlow(ScreenType.IFSC)

    private val _bankUIState = MutableStateFlow(BankUIState.getDefault())
    val bankUIState = _bankUIState.asStateFlow()

    private val _bankUISideEffectChannel = Channel<BankUISideEffect>()
    val bankUISideEffectFlow = _bankUISideEffectChannel.receiveAsFlow()

    private val bankFilterInfoFlow = MutableStateFlow(BankFilterInfo())

    private val bankSwiftCodePager by lazy {
        Pager(config = PagingConfig(100, prefetchDistance = 20, enablePlaceholders = true),
            pagingSourceFactory = {
                getBankSwiftPagingSource()
            })
    }

    private val bankDetailPager by lazy {
        Pager(config = PagingConfig(500, prefetchDistance = 100, enablePlaceholders = true),
            pagingSourceFactory = {
                getBankDetailsPagingSource()
            })
    }

    private val filterBankDetailPager by lazy {
        Pager(config = PagingConfig(500, prefetchDistance = 100), pagingSourceFactory = {
            getFilteredBankDetailsPagingSource()
        })
    }

    private val filterBankSwiftPager by lazy {
        Pager(config = PagingConfig(100, prefetchDistance = 20), pagingSourceFactory = {
            getFilteredBankSwiftPagingSource()
        })
    }

    private val bankInfoPager by lazy {
        Pager(config = PagingConfig(100, prefetchDistance = 20), pagingSourceFactory = {
            getBankInfoPagingSource()
        })
    }

    val bankDetailsPagingFlow = bankDetailPager.flow.map {
        getItemData(it) { detail ->
            detail.toBankDetailInfo()
        }
    }

    val bankSwiftCodePagingFloe = bankSwiftCodePager.flow.map {
        getItemData(it) { detail ->
            detail.toBankDetailInfo()
        }
    }

    val filterBankDetailsPagingFlow : Flow<PagingData<ItemBankData>> =
        filterBankDetailPager.flow.map {
            getItemData(it) { detail ->
                detail.toBankDetailInfo()
            }
        }.cachedIn(viewModelScope)

    val filterBankSwiftPagingFlow : Flow<PagingData<ItemBankData>> = filterBankSwiftPager.flow.map {
        getItemData(it) { detail ->
            detail.toBankDetailInfo()
        }
    }.cachedIn(viewModelScope)

    val bankInfoPagingFlow = bankInfoPager.flow.map {
        it.map {
            it.toBankInfo()
        }
    }

    val swiftCodeFilterInfoFlow = MutableStateFlow(SwiftCodeFilterInfo())

    val swiftSelectedTypeSetFlow = MutableStateFlow<Set<Int>>(setOf())

    private val backgroundScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun <T : Any> getItemData(
            pagingData : PagingData<T>,
            transform : (T) -> BankDetailInfo) : PagingData<ItemBankData> {
        return pagingData.map {
            ItemBankData.Detail(transform(it))
        }.insertSeparators { detail : ItemBankData.Detail?, detail2 : ItemBankData.Detail? ->
            if(detail?.bankDetailInfo?.bankId != detail2?.bankDetailInfo?.bankId) {
                ItemBankData.Header(
                    detail2?.bankDetailInfo?.bankId?:"",
                    detail2?.bankDetailInfo?.bankName
                        ?: "")
            } else {
                detail
            }
        }
    }

    init {
        viewModelScope.launch {
            bankDetailManager.isAllBankSelected().collectLatest { isAllBankSelected ->
                _bankUIState.update {
                    it.copy(isAllBankSelected = isAllBankSelected)
                }
            }
        }
    }

    fun updateBankFromRemote(onSuccess : () -> Unit) {
        backgroundScope.launch {
            bankDetailManager.updateBankListFromRemote {
                onBankDataLoaded(false)
                onSuccess()
            }
        }
    }

    fun onBankDataLoaded(isRefresh : Boolean) {
        _bankUIState.update {
            it.copy(isSyncing = true)
        }
        backgroundScope.launch {
            updateBankSwiftCodeRemote(isRefresh)
            updateBankPagingFromRemote(isRefresh)
        }
    }

    private fun updateBankSwiftCodeRemote(isRefresh : Boolean) {
        if(bankSwiftRemoteJob == null) {
            bankSwiftRemoteJob = backgroundScope.launch {
                bankDetailManager.updateBankSwiftFromRemote(apiKey, isRefresh) {

                }
                bankSwiftRemoteJob = null
            }
        }
    }

    private fun updateBankPagingFromRemote(isRefresh : Boolean) {
        if(bankDetailRemoteJob == null) {
            bankDetailRemoteJob = backgroundScope.launch {
                bankDetailManager.updateBankPagingFromRemote(isRefresh) {
                    _bankUIState.update {
                        it.copy(isSyncing = false)
                    }
                }

                bankDetailRemoteJob = null
            }
        }
    }

    private fun getBankDetailsPagingSource() : PagingSource<Int, GetEnabledBankDetailsByOffset> {
        val pagingSource = bankDetailManager.getBankDetailsPagingSource()
        shouldRefreshBankDetail = {
            pagingSource.invalidate()
        }
        return pagingSource
    }

    private fun getBankSwiftPagingSource() : PagingSource<Int, GetBankSwiftByOffset> {
        val pagingSource = bankDetailManager.getBankSwiftCodePagingSource()
        return pagingSource

    }

    private fun getBankInfoPagingSource() : PagingSource<Int, Banks> {
        val pagingSource =
            bankDetailManager.getBankInfoPagingSource(_bankUIState.value.bankSearchQuery)
        shouldRefreshBank = {
            pagingSource.invalidate()
        }
        return pagingSource
    }

    private fun getFilteredBankDetailsPagingSource() : PagingSource<Int, GetFilteredBankDetails> {
        val filterPagingSource =
            bankDetailManager.getFilteredBankDetailsPagingSource(bankFilterInfoFlow.value)

        shouldRefreshFilterBankDetail = {
            filterPagingSource.invalidate()
        }

        return filterPagingSource
    }

    private fun getFilteredBankSwiftPagingSource() : PagingSource<Int, GetFilteredBankSwift> {
        val filterPagingSource =
            bankDetailManager.getFilteredBankSwiftPagingSource(swiftCodeFilterInfoFlow.value)

        shouldRefreshFilterBankSwift = {
            filterPagingSource.invalidate()
        }

        return filterPagingSource
    }

    fun setEvent(bankUIEvent : BankUIEvent) {
        when(bankUIEvent) {

            is BankUIEvent.OnSelectAllBankChanged -> {
                viewModelScope.launch {
                    bankDetailManager.updateAllBankEnabled(bankUIEvent.isChecked)
                }
            }
            is BankUIEvent.OnBankInfoEnableChanged -> {
                viewModelScope.launch {
                    bankDetailManager.updateBankEnabled(
                        bankUIEvent.isEnabled, bankUIEvent.bankInfo.id)
                    refreshBankDetail()
                }
            }

            is BankUIEvent.OnSearchActiveChanged -> {
                _bankUIState.update {
                    it.copy(isSearchActive = bankUIEvent.isActive)
                }

                if(!bankUIEvent.isActive) {
                    clearAllFilter()
                }
            }

            is BankUIEvent.OnSearchQueryChanged -> {
                var bankFilterInfo = _bankUIState.value.bankFilterInfo
                bankFilterInfo = bankFilterInfo.copy(bankName = bankUIEvent.query)
                _bankUIState.update {
                    it.copy(
                        bankDetailSearchQuery = bankUIEvent.query, bankFilterInfo = bankFilterInfo)
                }

                updateFilterBank(bankFilterInfo, screenTypeFlow.value)
            }

            is BankUIEvent.OnBankSearchQueryChanged -> {
                _bankUIState.update {
                    it.copy(bankSearchQuery = bankUIEvent.query)
                }

                refreshBanks()
            }

            is BankUIEvent.OnFilterSelected -> {
                val set = _bankUIState.value.selectedFilterTypeSet.toHashSet()
                var bankFilterInfo = _bankUIState.value.bankFilterInfo


                if(bankUIEvent.enabled) {
                    set.add(bankUIEvent.filterType)
                    _bankUIState.update {
                        it.copy(selectedFilterTypeSet = set)
                    }
                } else {
                    set.remove(bankUIEvent.filterType)
                    bankFilterInfo = checkAndUpdateValue(bankUIEvent.filterType, bankFilterInfo, "")
                    _bankUIState.update {
                        it.copy(selectedFilterTypeSet = set, bankFilterInfo = bankFilterInfo)
                    }

                    updateFilterBank(bankFilterInfo, screenTypeFlow.value)
                }

            }

            is BankUIEvent.OnFilterValueChanged -> {
                var bankFilterInfo = _bankUIState.value.bankFilterInfo
                bankFilterInfo =
                    checkAndUpdateValue(bankUIEvent.filterType, bankFilterInfo, bankUIEvent.value)

                _bankUIState.update {
                    it.copy(bankFilterInfo = bankFilterInfo)
                }

                updateFilterBank(bankFilterInfo, screenTypeFlow.value)
            }

            is BankUIEvent.OnRemoveFilter -> {
                val set = _bankUIState.value.selectedFilterTypeSet.toHashSet()
                set.remove(bankUIEvent.filterType)

                var bankFilterInfo = _bankUIState.value.bankFilterInfo
                bankFilterInfo = checkAndUpdateValue(bankUIEvent.filterType, bankFilterInfo, "")

                _bankUIState.update {
                    it.copy(selectedFilterTypeSet = set, bankFilterInfo = bankFilterInfo)
                }

                updateFilterBank(bankFilterInfo, screenTypeFlow.value)
            }

            is BankUIEvent.OnOpenDrawer -> {
                _bankUIState.update {
                    it.copy(openDrawer = bankUIEvent.shouldOpen)
                }
            }

            is BankUIEvent.OnClearAllFilter -> {
                clearAllFilter()
            }

            is BankUIEvent.OnKeyBackPress -> {
                val set = _bankUIState.value.selectedFilterTypeSet.toHashSet()
                val lastValue = set.lastOrNull()
                if(lastValue != null) {
                    set.remove(lastValue)

                    var bankFilterInfo = _bankUIState.value.bankFilterInfo
                    bankFilterInfo = checkAndUpdateValue(lastValue, bankFilterInfo, "")

                    _bankUIState.update {
                        it.copy(selectedFilterTypeSet = set, bankFilterInfo = bankFilterInfo)
                    }

                    updateFilterBank(bankFilterInfo, screenTypeFlow.value)

                }
            }

            is BankUIEvent.OnBankDetailClicked -> {

            }

            is BankUIEvent.OnGetSwiftCodeClicked -> {
                val banDetailInfo = bankUIEvent.bankDetailInfo
                val swiftCodeFilterInfo =
                    SwiftCodeFilterInfo(banDetailInfo.bankName, banDetailInfo.city)
                swiftCodeFilterInfoFlow.value = swiftCodeFilterInfo
                swiftSelectedTypeSetFlow.value =
                    setOf(BankProUtils.TYPE_FILTER_SEARCH_CITY, BankProUtils.TYPE_FILTER_BANK_NAME)
                viewModelScope.launch {
                    _bankUISideEffectChannel.send(BankUISideEffect.NavigateSwiftCode)
                }

                updateFilteredBankSwift(swiftCodeFilterInfo)
            }

            BankUIEvent.OnSwiftClearAllFilter -> {
                swiftCodeFilterInfoFlow.value = SwiftCodeFilterInfo()
                updateFilteredBankSwift(SwiftCodeFilterInfo())

            }

            is BankUIEvent.OnSwiftFilterSelected -> {
                val set = swiftSelectedTypeSetFlow.value.toHashSet()
                var swiftCodeFilterInfo = swiftCodeFilterInfoFlow.value


                if(bankUIEvent.enabled) {
                    set.add(bankUIEvent.filterType)
                } else {
                    set.remove(bankUIEvent.filterType)
                    swiftCodeFilterInfo =
                        checkAndUpdateValue(bankUIEvent.filterType, swiftCodeFilterInfo, "")
                }
                swiftCodeFilterInfoFlow.value = swiftCodeFilterInfo
                swiftSelectedTypeSetFlow.value = set

                updateFilteredBankSwift(swiftCodeFilterInfo)

            }

            is BankUIEvent.OnSwiftFilterValueChanged -> {
                var swiftCodeFilterInfo = swiftCodeFilterInfoFlow.value
                swiftCodeFilterInfo = checkAndUpdateValue(
                    bankUIEvent.filterType, swiftCodeFilterInfo, bankUIEvent.value)

                swiftCodeFilterInfoFlow.value = swiftCodeFilterInfo
                updateFilteredBankSwift(swiftCodeFilterInfo)

            }

            BankUIEvent.OnSwiftKeyBackPress -> {

            }

            is BankUIEvent.OnSwiftRemoveFilter -> {
                val set = swiftSelectedTypeSetFlow.value.toHashSet()
                set.remove(bankUIEvent.filterType)

                var swiftCodeFilterInfo = swiftCodeFilterInfoFlow.value
                swiftCodeFilterInfo =
                    checkAndUpdateValue(bankUIEvent.filterType, swiftCodeFilterInfo, "")

                swiftCodeFilterInfoFlow.value = swiftCodeFilterInfo
                swiftSelectedTypeSetFlow.value = set

                updateFilteredBankSwift(swiftCodeFilterInfo)

            }

            is BankUIEvent.OnScreenTypeChanged -> {
                _bankUIState.update {
                    it.copy(selectedScreenType = bankUIEvent.screenType)
                }
                screenTypeFlow.value = bankUIEvent.screenType
                clearAllFilter()
            }
        }
    }

    private fun clearAllFilter() {
        val bankFilterInfo = BankFilterInfo()
        _bankUIState.update {
            it.copy(selectedFilterTypeSet = setOf(), bankFilterInfo = bankFilterInfo)
        }

        updateFilterBank(bankFilterInfo, screenTypeFlow.value)
    }

    private fun updateFilterBank(bankFilterInfo : BankFilterInfo, screenType : ScreenType) {
        if(screenType == ScreenType.IFSC) {
            bankFilterInfoFlow.value = bankFilterInfo
            updateFilteredBankDetails(bankFilterInfo)
        } else {
            val swiftCodeFilterInfo = SwiftCodeFilterInfo(
                bankFilterInfo.bankName, bankFilterInfo.city, bankFilterInfo.branch,
                bankFilterInfo.country, swiftCode = bankFilterInfo.swiftCode)

            swiftCodeFilterInfoFlow.value = swiftCodeFilterInfo
            updateFilteredBankSwift(swiftCodeFilterInfo)
        }
    }

    private fun updateFilteredBankDetails(bankFilterInfo : BankFilterInfo) {
        refreshFilteredBankDetail()
    }

    private fun updateFilteredBankSwift(swiftCodeFilterInfo : SwiftCodeFilterInfo) {
        refreshFilteredBankSwift()
    }

    private fun refreshBankDetail() {
        shouldRefreshBankDetail?.invoke()
    }

    private fun refreshFilteredBankDetail() {
        shouldRefreshFilterBankDetail?.invoke()
    }

    private fun refreshFilteredBankSwift() {
        shouldRefreshFilterBankSwift?.invoke()
    }

    private fun refreshBanks() {
        shouldRefreshBank?.invoke()
    }

    override fun onCleared() {
        super.onCleared()
        bankSwiftRemoteJob?.cancel()
        bankSwiftRemoteJob = null

        bankDetailRemoteJob?.cancel()
        bankDetailRemoteJob = null
    }

}