package com.jackson.shared.common.bankdetail.data.model

data class PaginationItems<T>(
    val items: List<T>,
    val page: Int,
    val total: Long
)