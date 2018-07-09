package com.x1unix.cashlytics.core.providers

/**
 * Metadata extractor result
 *
 * @param result Metadata parse result
 * @param processedSubstring Substring that used to extract metadata
 * @param origin original source string
 */
data class MetadataParseResult<out T>(val result: T, val processedSubstring: String, val origin: String) {
    /**
     * Next substring that left to process
     */
    val nextUnprocessedChunk: String
        get() = origin.substring(processedSubstring.length).trim()
}