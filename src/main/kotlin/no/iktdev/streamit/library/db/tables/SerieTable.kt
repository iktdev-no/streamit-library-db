package no.iktdev.streamit.library.db.tables

import no.iktdev.streamit.library.db.tables.catalog.defaultExpression
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.time.LocalDateTime

object serie: IntIdTable() {
    val title: Column<String?> = varchar("title", 250).nullable()
    val episode: Column<Int> = integer("episode")
    val season: Column<Int> = integer("season")
    val collection: Column<String> = varchar("collection", 250)
    val video: Column<String> = varchar("video", 250).uniqueIndex()
    val added: Column<LocalDateTime> = datetime("added").defaultExpression(CurrentDateTime)
}