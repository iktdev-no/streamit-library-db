package no.iktdev.streamit.library.db.objects

import no.iktdev.streamit.library.db.tables.Shared
import no.iktdev.streamit.library.db.tables.content.ProgressTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class ProgressTableObject(
    val id: Int,
    val guid: String,
    val type: String,
    val title: String,
    val collection: String?,
    val episode: Int?,
    val season: Int?,
    val video: String,
    val progress: Int,
    val duration: Int,
    val played: LocalDateTime?
) {
    companion object {
        fun fromRow(row: ResultRow): ProgressTableObject {
            return ProgressTableObject(
                id = row[ProgressTable.id].value,
                guid = row[ProgressTable.guid],
                type = row[ProgressTable.type],
                title = row[ProgressTable.title],
                collection = row[ProgressTable.collection],
                episode = row[ProgressTable.episode],
                season = row[ProgressTable.season],
                video = row[ProgressTable.video],
                progress = row[ProgressTable.progress],
                duration = row[ProgressTable.duration],
                played = row[ProgressTable.played]
            )
        }
    }
}