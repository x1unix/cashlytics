package com.x1unix.cashlytics.core.storage.repository

import com.couchbase.lite.*
import com.x1unix.cashlytics.core.storage.mappers.Mapper

abstract class Repository<T>(protected val database: Database) {
    protected abstract val mapper: Mapper<T>

    /**
     * Inserts an item to a bucket
     *
     * @return Inserted item's ID
     */
    fun addItem(item: T): String {
        val doc = mapper.toDocument(item)
        database.save(doc)

        return doc.id
    }

    /**
     * Performs bulk add
     *
     * @param items Items list
     */
    fun bulkAdd(items: List<T>) {
        items.map{ item: T -> mapper.toDocument(item)}
                .forEach{ doc: MutableDocument -> database.save(doc) }
    }

    /**
     * Gets all items in the collection
     */
    fun getAll(): List<T> {
        val results = getQuery().execute()

        return results.allResults().map{ result -> mapper.fromResult(result)}
    }

    /**
     * Creates a new query
     */
    protected fun getQuery(): Where {
        return getQuery(null)
    }

    /**
     * Creates a new query
     *
     * @param wheres Additional WHERE query
     */
    protected fun getQuery(wheres: Expression?): Where {
        var where = Expression.property(Mapper.OBJECT_TYPE).equalTo(Expression.string(mapper.objectType))

        if (wheres != null) {
            where = where.and(wheres)
        }

        return QueryBuilder.select(SelectResult.all(), SelectResult.expression(Meta.id))
                .from(DataSource.database(database))
                .where(where)
    }

    /**
     * Gets full property name with data property prefix
     */
    protected fun property(name: String): String {
        return ".${Mapper.DATA}.$name"
    }
}