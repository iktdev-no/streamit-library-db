package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object users : Table() {
    val guid: Column<String> = varchar("guid", 50)
    val name: Column<String> = varchar("name", 50).uniqueIndex()
    val image: Column<String> = varchar("image", 200)
}