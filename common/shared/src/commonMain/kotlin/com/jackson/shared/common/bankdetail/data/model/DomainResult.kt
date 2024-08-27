package com.jackson.shared.common.bankdetail.data.model


sealed class DomainResult<out T> {

    data class Success<T>(val data: T) : DomainResult<T>()

    data class Error(val remoteError: String) : DomainResult<Nothing>()
}