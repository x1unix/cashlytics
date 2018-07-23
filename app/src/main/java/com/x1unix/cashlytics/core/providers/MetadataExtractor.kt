package com.x1unix.cashlytics.core.providers

/**
 * Payment data extractor interface
 *
 * @param T the output result type
 */
interface MetadataExtractor<out T> {
    /**
     * Extracts data from message (sub)string
     *
     * @param message message
     * @param baseDataOnly Extract only base data
     * @throws com.x1unix.cashlytics.exceptions.NoMatchFoundException
     * @return Metadata parse result with additional information
     */
    fun extractData(message: String, baseDataOnly: Boolean) : MetadataParseResult<T>
}