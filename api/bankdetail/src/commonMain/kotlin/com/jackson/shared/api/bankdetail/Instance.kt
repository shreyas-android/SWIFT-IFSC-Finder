package com.jackson.shared.api.bankdetail


import com.jackson.shared.common.bankdetail.remote.client.NetworkClient
import com.jackson.shared.api.bankdetail.remote.endpoints.BankRemoteServiceImpl
import com.jackson.shared.api.bankdetail.remote.path.RemoteUrlPath
import com.jackson.shared.api.bankdetail.repository.BankRemoteRepositoryImpl
import com.jackson.shared.common.bankdetail.jsonreader.SharedFileReader

internal class Instance(private val sharedFileReader : SharedFileReader) {

    val bankRepository by lazy {
        BankRemoteRepositoryImpl(bankRemoteService, sharedFileReader)
    }

    private val remoteUrlPath by lazy {
        RemoteUrlPath()
    }

    private val bankRemoteService by lazy {
        BankRemoteServiceImpl(
            networkClient,
            remoteUrlPath)
    }

    private val networkClient by lazy {
        NetworkClient()
    }
}