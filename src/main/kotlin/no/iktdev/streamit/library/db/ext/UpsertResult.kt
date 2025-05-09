package no.iktdev.streamit.library.db.ext

import org.jetbrains.exposed.sql.statements.InsertStatement

sealed class UpsertResult {
    data class Inserted(val statement: InsertStatement<Long>) : UpsertResult()
    object Updated : UpsertResult()
    object Skipped : UpsertResult() // hvis du ønsker en tredje variant
}