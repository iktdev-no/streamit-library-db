package no.iktdev.streamit.library.db.tables.other

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object DataAudioTable : IntIdTable(name = "MediaDataAudio") {
    val file: Column<String> = varchar("source", 200).uniqueIndex() // Currently Audio Stream is embedded in video file. Might change at a later date
    val codec: Column<String> = varchar("codec", 12)
    val channels: Column<Int?> = integer("channels").nullable()
    val sample_rate: Column<Int?> = integer("sampleRate").nullable()
    val layout: Column<String?> = varchar("layout", 8).nullable()
    val language: Column<String> = varchar("language", 6)
}