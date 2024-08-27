package com.jackson.shared.common.bankdetail.exception

data class NetworkResponseException(val calendarError: String) : Exception(calendarError)


