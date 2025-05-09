package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDateTime

data class InternalSerieTableDto(
    val title: String?,
    val episode: Int,
    val season: Int,
    val collection: String,
    val video: String,
    val added: LocalDateTime
)

data class InternalCatalogTableDto(
    val title: String,
    val cover: String?,
    val type: String,
    val collection: String,
    val iid: Int?,
    val genres: String?,
    val added: LocalDateTime
)

fun ResultRow.fromRowToInternalSerieTableDto() = InternalSerieTableDto(
    title = this[SerieTable.title],
    episode = this[SerieTable.episode],
    season = this[SerieTable.season],
    collection = this[SerieTable.collection],
    video = this[SerieTable.video],
    added = this[SerieTable.added]
)