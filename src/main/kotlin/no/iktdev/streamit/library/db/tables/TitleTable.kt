package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TitleTable: IntIdTable() {
    val collection: Column<String> = varchar("collection", 250)
    val title: Column<String> = varchar("title", 250)

    init {
        uniqueIndex(collection, title)
    }
}