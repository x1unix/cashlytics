package com.x1unix.cashlytics.providers.kredobank.extractors

interface MetadataExtractor<out T> {
    fun extractData(message: String) : T
}