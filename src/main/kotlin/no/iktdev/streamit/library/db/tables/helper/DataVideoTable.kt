package no.iktdev.streamit.library.db.tables.helper

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object data_video : IntIdTable() {
    val file: Column<String> = varchar("source", 200).uniqueIndex()
    val codec: Column<String> = varchar("codec", 12)
    val pixelFormat: Column<String> = varchar("pixelFormat", 12)
    val colorSpace: Column<String?> = varchar("colorSpace", 8).nullable()
}