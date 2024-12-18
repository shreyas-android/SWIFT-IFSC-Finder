package com.jackson.shared.common.bankdetail.flow

import kotlinx.coroutines.flow.MutableStateFlow

actual class CommonMutableStateFlow<T> actual constructor(
        private val flow: MutableStateFlow<T>
) : MutableStateFlow<T> by flow