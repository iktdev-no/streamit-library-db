package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TitleTable: IntIdTable(name = "Titles") {
    val masterTitle: Column<String> = varchar("masterTitle", 250)
    val alternativeTitle: Column<String> = varchar("alternativeTitle", 250)

    init {
        uniqueIndex(masterTitle, alternativeTitle)
    }
}