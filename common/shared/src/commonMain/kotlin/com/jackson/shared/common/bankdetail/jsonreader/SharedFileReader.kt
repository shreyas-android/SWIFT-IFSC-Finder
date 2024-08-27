package com.jackson.shared.common.bankdetail.jsonreader

// in src/commonMain/kotlin
interface SharedFileReader{
    fun loadJsonFile(fileName: String): String?
}