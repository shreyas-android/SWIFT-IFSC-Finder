package com.jackson.shared.common.bankdetail.jsonreader

import android.content.Context
import android.util.Log
import java.io.InputStreamReader

class AndroidSharedFileReader(private val context : Context):SharedFileReader{
    override fun loadJsonFile(fileName: String): String {
        /*val resourceId = context.resources.getIdentifier(fileName, "raw", context.packageName)
        val inputStream = context.resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use { it.readText() }*/

        val pathName = "commonMain/resources/$fileName"
        val stream = javaClass.classLoader?.getResourceAsStream(pathName)
        Log.d("CHECKSTREAMDATA","CHEKCIG THE STREAM DATA = $stream")
        return stream.use { stream ->
            InputStreamReader(stream).use { reader ->
                reader.readText()
            }
        }
    }
}