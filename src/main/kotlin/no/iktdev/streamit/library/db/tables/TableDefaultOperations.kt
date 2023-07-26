package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update

import org.jetbrains.exposed.sql.transactions.transaction

open class TableDefaultOperations<T: Table> {

}

fun <T> withTransaction(block: () -> T): T? {
    return try {
        transaction { block() }
    } catch (e: Exception) {
        e.printStackTrace()
        // log the error here
        null
    }
}

fun <T> insertWithSuccess(block: () -> T): Boolean {
    return try {
        transaction { block }
        true
    } catch (e : Exception) {
        e.printStackTrace()
        false
    }
}

