package com.jackson.shared.common.bankdetail.data.model

sealed class UIRemoteResult<out T> {

    object Loading : UIRemoteResult<Nothing>()

    data class Success<T>(val data: T) : UIRemoteResult<T>()

    data class Error(val remoteError: String) : UIRemoteResult<Nothing>()
}