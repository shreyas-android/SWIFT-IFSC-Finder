package com.jackson.shared.common.bankdetail.data.model

sealed class Status<T> {

    data class Started<T>(val time: Long = 0L) : Status<T>()

    data class Completed<T>(val data: T) : Status<T>()
}