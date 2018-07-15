package com.x1unix.cashlytics.core.storage.mappers

import com.couchbase.lite.Dictionary
import com.couchbase.lite.Document
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.Result
import java.util.*

/**
 * Abstract POJO to Couchbase document mapper.
 * Allows to wrap POJO to Couchbase document and vice-versa
 */
abstract class Mapper<T> {

    /**
     * Object type name.
     * Used to identify object type in the bucket
     */
    protected abstract val objectType: String

    /**
     * Random document ID for new documents
     */
    private val randomDocumentId: String
        get() = "$objectType.${UUID.randomUUID()}"

    /**
     * Convert object to Document
     *
     * @param item Object
     */
    fun toDocument(item: T): MutableDocument {
        val data = wrap(item)
        return MutableDocument(randomDocumentId)
                .setString(OBJECT_TYPE, objectType)
                .setDictionary(DATA, data)
    }

    /**
     * Convert document to object
     *
     * @param doc Couchbase document
     */
    fun fromResult(result: Result): T {
        val doc = result.getDictionary(0)   // Get document data
        val id = result.getString(1)        // Get document ID
        val type = doc.getString(OBJECT_TYPE)

        if (type != objectType) {
            throw IllegalArgumentException("Document type mismatch: '$type' (expected '$objectType')")
        }

        val data = doc.getDictionary(DATA)

        return unwrap(data, id)
    }

    protected abstract fun unwrap(dict: Dictionary, itemId: String): T

    protected abstract fun wrap(item: T): Dictionary

    companion object {
        const val OBJECT_TYPE = "type"
        const val DATA = "data"
    }
}