package com.jackson.shared.common.bankdetail.flow

import kotlinx.coroutines.flow.MutableStateFlow

class IOSMutableStateFlow<T>(
        initialValue: T
) : CommonMutableStateFlow<T>(MutableStateFlow(initialValue))