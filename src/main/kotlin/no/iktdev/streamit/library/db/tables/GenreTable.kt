package no.iktdev.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object GenreTable : IntIdTable() {
    val genre: Column<String> = varchar("genre", 50).uniqueIndex()
}