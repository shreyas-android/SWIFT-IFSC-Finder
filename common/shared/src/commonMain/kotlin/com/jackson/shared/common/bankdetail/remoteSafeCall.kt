package com.jackson.shared.common.bankdetail

import com.jackson.shared.common.bankdetail.exception.NetworkResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

inline fun <T> remoteSafeCall(action: () -> HttpResponse, onSuccess: (value: HttpResponse) -> T,
                              defaultErrorValue:String = RemoteUtils.DEFAULT_ERROR_MESSAGE): RemoteResponse<T> {
    return runCatching {
        action()
    }.fold(onSuccess = {
        RemoteResponse.Success(onSuccess(it))
    }, onFailure = {
            when (it) {
            is NetworkResponseException -> {
                RemoteResponse.Error(
                    it.calendarError
                )
            }
            else ->{
                RemoteResponse.Error(it.message ?: defaultErrorValue)
            }
        }
    })
}

suspend fun <T> Flow<T>.safeCollectFlowFirst(): T? {
    return runCatching {
        first()
    }.fold(onSuccess = {
        it
    }, onFailure = {
        null
    })
}