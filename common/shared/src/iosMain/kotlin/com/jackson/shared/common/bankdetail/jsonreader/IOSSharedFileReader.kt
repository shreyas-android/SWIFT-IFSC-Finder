package com.jackson.shared.common.bankdetail.jsonreader

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile
import platform.darwin.NSObject
import platform.darwin.NSObjectMeta

class IOSSharedFileReader: SharedFileReader {
    private val bundle: NSBundle = NSBundle.bundleForClass(BundleMarker)

    @OptIn(ExperimentalForeignApi::class)
    override fun loadJsonFile(fileName: String): String {
        val path = NSBundle.mainBundle.pathForResource(fileName, ofType = "json")
        return path?.let {
            NSString.stringWithContentsOfFile(it, encoding = NSUTF8StringEncoding, error = null) as String
        } ?: throw IllegalArgumentException("File not found: $fileName")
    }

    private class BundleMarker : NSObject() {
        companion object : NSObjectMeta()
    }
}