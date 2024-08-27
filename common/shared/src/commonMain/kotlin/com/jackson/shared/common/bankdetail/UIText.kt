package com.jackson.shared.common.bankdetail

sealed class UIText {

    object Empty : UIText()
    data class DynamicString(val value: String) : UIText()

    class SharedStringResource(val resource: String, vararg val args: Any) : UIText()

}