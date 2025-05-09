package no.iktdev.streamit.library.db.tables.user

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object ProfileImageTable : IntIdTable(name = "ProfileImage") {
    val filename: Column<String> = varchar("fileName", 250)
    val added: Column<LocalDateTime> = datetime("added").defaultExpression(CurrentDateTime)
}