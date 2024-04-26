package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object resumeOrNext : IntIdTable() {
    val userId: Column<String> = varchar("userId", userIdLength)
    val ignore: Column<Boolean> = bool("ignore").default(false)

    val type: Column<String> = varchar("type", 10)
    val collection: Column<String> = varchar("collection", 250)
    val episode: Column<Int?> = integer("episode").nullable()
    val season: Column<Int?> = integer("season").nullable()
    val video: Column<String> = varchar("video", 250)
    val updated: Column<LocalDateTime> = datetime("played").defaultExpression(CurrentDateTime)

    init {
        uniqueIndex(collection, type, userId)
    }
}