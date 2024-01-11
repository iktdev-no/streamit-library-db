package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

val userIdLength: Int = 50
object progress : IntIdTable() {
    val guid: Column<String> = varchar("guid", userIdLength)
    val type: Column<String> = varchar("type", 10)
    val title: Column<String> = varchar("title", 100)
    val collection: Column<String?> = varchar("collection", 250).nullable()
    val episode: Column<Int?> = integer("episode").nullable()
    val season: Column<Int?> = integer("season").nullable()
    val video: Column<String> = varchar("video", 100)
    val progress: Column<Int> = integer("progress")
    val duration: Column<Int> = integer("duration")
    val played: Column<LocalDateTime?> = datetime("played").nullable()

    init {

    }
}