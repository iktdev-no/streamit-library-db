package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object profile_image : IntIdTable() {
    val filename: Column<String> = varchar("title", 250)
    val added: Column<LocalDateTime> = datetime("added").defaultExpression(CurrentDateTime)
}