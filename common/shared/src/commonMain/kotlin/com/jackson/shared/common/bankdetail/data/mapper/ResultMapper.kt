package com.jackson.shared.common.bankdetail.data.mapper

import com.jackson.shared.common.bankdetail.RemoteResponse
import com.jackson.shared.common.bankdetail.data.model.Status
import com.jackson.shared.common.bankdetail.data.model.UIRemoteResult

fun <T, R> RemoteResponse<T>.toUIRemoteResult(transform: (T) -> R): UIRemoteResult<R> {
    return when (this) {
        is RemoteResponse.Success -> {
            UIRemoteResult.Success(transform(this.data))
        }

        is RemoteResponse.Error -> {
            UIRemoteResult.Error(this.calendarRemoteError)
        }
    }
}

fun <T> RemoteResponse<T>.toUIRemoteResult(): UIRemoteResult<T> {
    return when (this) {
        is RemoteResponse.Success -> {
            UIRemoteResult.Success(this.data)
        }

        is RemoteResponse.Error -> {
            UIRemoteResult.Error(this.calendarRemoteError)
        }
    }
}

fun <T, R> Status<RemoteResponse<T>>.toUIRemoteResult(transform: (T) -> R): UIRemoteResult<R> {
    return when (this) {
        is Status.Completed -> {
            when (val remoteResponse = this.data) {
                is RemoteResponse.Success -> {
                    UIRemoteResult.Success(transform(remoteResponse.data))
                }

                is RemoteResponse.Error -> {
                    UIRemoteResult.Error(remoteResponse.calendarRemoteError)
                }
            }
        }

        is Status.Started -> {
            UIRemoteResult.Loading
        }
    }
}