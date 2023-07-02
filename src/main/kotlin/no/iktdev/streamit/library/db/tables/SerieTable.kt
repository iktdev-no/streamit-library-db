package no.iktdev.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object SerieTable : IntIdTable() {
    val title: Column<String> = varchar("title", 250)
    val episode: Column<Int> = integer("episode")
    val season: Column<Int> = integer("season")
    val collection: Column<String> = varchar("collection", 250)
    val video: Column<String> = varchar("video", 250).uniqueIndex()
    val added: Column<Instant> = timestamp("added")
}