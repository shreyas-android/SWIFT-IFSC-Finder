package com.jackson.shared.common.bankdetail.exception


import com.jackson.shared.common.bankdetail.RemoteUtils
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

// TODO: Should handle empty message for error
object ApiExceptionHandler {

    private suspend fun throwError(exceptionResponse: HttpResponse) {
        val response = exceptionResponse.bodyAsText()
        throw NetworkResponseException(response)
    }

    suspend fun handleServerException(exceptionResponse: HttpResponse) {
        throwError(exceptionResponse)
    }

    suspend fun handleRedirectResponseException(exceptionResponse: HttpResponse) {
        throwError(exceptionResponse)
    }

    suspend fun handleResponseException(exceptionResponse: HttpResponse) {
        throwError(exceptionResponse)
    }

    suspend fun handleClientRequestException(exceptionResponse: HttpResponse) {
        throwError(exceptionResponse)
    }

    fun handleRequestTimeoutException(exception: Throwable) {
        throw NetworkResponseException(
           RemoteUtils.DEFAULT_ERROR_MESSAGE
        )
    }

    fun handleConnectionTimeoutException(exception: Throwable) {
        throw NetworkResponseException(
            RemoteUtils.DEFAULT_ERROR_MESSAGE
        )
    }

    fun handleSocketTimeoutException(exception: Throwable) {
        throw NetworkResponseException(
            RemoteUtils.DEFAULT_ERROR_MESSAGE
        )
    }


}
