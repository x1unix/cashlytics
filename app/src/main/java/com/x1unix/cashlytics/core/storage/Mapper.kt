package com.x1unix.cashlytics.core.storage

import com.couchbase.lite.Document
import com.couchbase.lite.MutableDocument

interface Mapper<T> {
    fun toDocument(item: T): MutableDocument

    fun fromDocument(doc: Document): T
}