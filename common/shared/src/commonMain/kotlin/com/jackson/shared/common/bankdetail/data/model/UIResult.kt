package com.jackson.shared.common.bankdetail.data.model

import com.jackson.shared.common.bankdetail.UIText

sealed class UIResult<out T> {

    object Loading : UIResult<Nothing>()

    data class Success<T>(val data: T) : UIResult<T>()

    data class Error(val message: UIText) : UIResult<Nothing>()
}