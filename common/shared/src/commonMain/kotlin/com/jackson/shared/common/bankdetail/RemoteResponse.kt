package com.jackson.shared.common.bankdetail

sealed class RemoteResponse<out T> {
    data class Success<T>(val data: T) : RemoteResponse<T>()
    data class Error(val calendarRemoteError: String) : RemoteResponse<Nothing>()
}