package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object genre: IntIdTable() {
    val genre: Column<String> = varchar("genre", 50).uniqueIndex()
}