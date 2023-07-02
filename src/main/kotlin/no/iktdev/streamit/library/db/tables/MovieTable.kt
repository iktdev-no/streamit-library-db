package no.iktdev.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object MovieTable : IntIdTable() {
    val video: Column<String> = varchar("video", 250).uniqueIndex()
}