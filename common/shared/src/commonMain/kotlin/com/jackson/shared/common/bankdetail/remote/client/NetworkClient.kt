package com.jackson.shared.common.bankdetail.remote.client

import com.jackson.shared.common.bankdetail.exception.ApiExceptionHandler
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NetworkClient {

    fun getNetworkClient(): HttpClient = httpClient

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("HTTP call =  $message")
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout){
            requestTimeoutMillis = 1000 * 60
            connectTimeoutMillis = 1000 * 60
            socketTimeoutMillis = 1000 * 60
        }
        expectSuccess = true
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when (exception) {
                    is ClientRequestException -> ApiExceptionHandler.handleClientRequestException(exception.response)
                    is ServerResponseException -> ApiExceptionHandler.handleServerException(exception.response)
                    is RedirectResponseException -> ApiExceptionHandler.handleRedirectResponseException(exception.response)
                    is ResponseException -> ApiExceptionHandler.handleResponseException(exception.response)
                    is HttpRequestTimeoutException ->  ApiExceptionHandler.handleRequestTimeoutException(exception)
                    is ConnectTimeoutException -> { ApiExceptionHandler.handleConnectionTimeoutException(exception) }
                    is SocketTimeoutException -> { ApiExceptionHandler.handleSocketTimeoutException(exception) }
                }
            }
        }
    }

}