package no.iktdev.streamit.library.db

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.sql.SQLIntegrityConstraintViolationException

open class TableDefaultOperations<T : Table> {

}

data class TransactionResult<T>(
    val result: T,
    val exception: Exception? = null
)

fun <T> withDirtyRead(db: Database? = null, block: () -> T, onError: ((Exception) -> Unit)? = null): T? {
    return try {
        transaction(db = db, transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED) {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
                // log the error here or handle the exception as needed
                throw e // Optionally, you can rethrow the exception if needed
            }
        }
    } catch (e: Exception) {
        if (onError == null) {
            e.printStackTrace()
        } else {
            onError.invoke(e)
        }
        null
    }
}


fun <T> withTransaction(db: Database? = null, block: () -> T, onError: ((Exception) -> Unit)? = null): T? {
    return try {
        transaction(db) {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
                // log the error here or handle the exception as needed
                throw e // Optionally, you can rethrow the exception if needed
            }
        }
    } catch (e: Exception) {
        if (onError == null) {
            e.printStackTrace()
        } else {
            onError.invoke(e)
        }
        null
    }
}


fun <T> insertWithSuccess(db: Database? = null, block: () -> T, onError: ((Exception) -> Unit)? = null): Boolean {
    return try {
        transaction(db) {
            try {
                block()
                commit()
            } catch (e: Exception) {
                e.printStackTrace()
                // log the error here or handle the exception as needed
                throw e // Optionally, you can rethrow the exception if needed
            }
        }
        true
    } catch (e: Exception) {
        if (onError == null) {
            e.printStackTrace()
        } else {
            onError.invoke(e)
        }
        false
    }
}

fun <T> executeOrException(db: Database? = null, rollbackOnFailure: Boolean = false, block: () -> T): Exception? {
    return try {
        transaction(db) {
            try {
                block()
                commit()
                null
            } catch (e: Exception) {
                // log the error here or handle the exception as needed
                if (rollbackOnFailure)
                    rollback()
                e

            }
        }
    } catch (e: Exception) {
        return e
    }
}

fun <T> executeWithResult(db: Database? = null, block: () -> T, onError: ((Exception) -> Unit)? = null): T? {
    return try {
        transaction(db) {
            try {
                val res = block()
                commit()
                res
            } catch (e: Exception) {
                // log the error here or handle the exception as needed
                rollback()
                throw e
            }
        }
    } catch (e: Exception) {
        if (onError == null) {
            e.printStackTrace()
        } else {
            onError.invoke(e)
        }
        return null
    }
}

fun <T> executeWithStatus(db: Database? = null, block: () -> T, onError: ((Exception) -> Unit)? = null): Boolean {
    return try {
        transaction(db) {
            try {
                block()
                commit()
            } catch (e: Exception) {
                e.printStackTrace()
                // log the error here or handle the exception as needed
                throw e // Optionally, you can rethrow the exception if needed
            }
        }
        true
    } catch (e: Exception) {
        if (onError == null) {
            e.printStackTrace()
        } else {
            onError.invoke(e)
        }
        false
    }
}

fun Exception.isExposedSqlException(): Boolean {
    return this is ExposedSQLException
}

fun ExposedSQLException.isCausedByDuplicateError(): Boolean {
    return if (this.cause is SQLIntegrityConstraintViolationException) {
        return this.errorCode == 1062
    } else false
}


