package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object titles: IntIdTable() {
    val masterTitle: Column<String> = varchar("masterTitle", 250)
    val title: Column<String> = varchar("title", 250)
    val type: Column<Int> = integer("type").default(3)

    init {
        uniqueIndex(masterTitle, title, type)
    }
}