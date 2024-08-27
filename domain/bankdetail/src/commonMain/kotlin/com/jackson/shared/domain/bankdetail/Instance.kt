package com.jackson.shared.domain.bankdetail

import com.jackson.shared.data.bankdetail.repository.BankDataRepository
import com.jackson.shared.domain.bankdetail.manager.BankDetailManager
import com.jackson.shared.domain.bankdetail.manager.BankDetailManagerImpl
import com.jackson.shared.api.bankdetail.repository.BankRemoteRepository

internal object Instance {


    private val bankDataRepository by lazy {
        BankDataRepository.getInstance()
    }

    private val bankRemoteRepository by lazy {
        BankRemoteRepository.getInstance()
    }

    val bankDetailManager:BankDetailManager by lazy {
        BankDetailManagerImpl(bankRemoteRepository, bankDataRepository)
    }
}