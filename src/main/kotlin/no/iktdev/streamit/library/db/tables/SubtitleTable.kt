package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object subtitle : IntIdTable() {
    val associatedWithVideo: Column<String> = varchar("associatedWithVideo", 250)
    val language: Column<String> = varchar("language", 16)
    val subtitle: Column<String> = varchar("subtitle", 250)
    val collection: Column<String> = varchar("collection", 250)
    val format: Column<String> = varchar("format", 12)
    init {
        uniqueIndex(associatedWithVideo, language, format)
    }
}