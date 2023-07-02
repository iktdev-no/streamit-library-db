package no.iktdev.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object SummaryTable : IntIdTable() {
    val description: Column<String> = text("description")
    val language: Column<String> = varchar("language", 16)
    val cid: Column<Int> = integer("cid")

    init {
        uniqueIndex(language, cid)
    }
}