package no.iktdev.streamit.library.db.tables.content

import no.iktdev.streamit.library.db.ext.UpsertResult
import no.iktdev.streamit.library.db.tables.Shared
import no.iktdev.streamit.library.db.timestampToLocalDateTime
import no.iktdev.streamit.library.db.withTransaction
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.PreparedStatement
import java.time.LocalDateTime
import kotlin.String


object ProgressTable : IntIdTable(name = "Progress") {

    val guid: Column<String> = varchar("guid", Shared.userIdLength)
    val type: Column<String> = varchar("type", 10)
    val title: Column<String> = varchar("title", 250)
    val collection: Column<String?> = varchar("collection", 250).nullable()
    val episode: Column<Int?> = integer("episode").nullable()
    val season: Column<Int?> = integer("season").nullable()
    val video: Column<String> = varchar("video", 250)
    val progress: Column<Int> = integer("progress")
    val duration: Column<Int> = integer("duration")
    val played: Column<LocalDateTime?> = datetime("played").nullable()

    init {
        uniqueIndex("unique_progress_on_each_user", guid, title, collection, type, video)
        index(true, guid, collection, season, episode)
    }

    /**
     * @return All progress record for user
     */
    fun selectRecords(userId: String): Query {
        return ProgressTable.selectAll()
            .where { guid eq userId }
    }

    fun selectMovieRecords(userId: String): Query {
        return selectRecords(userId)
            .andWhere { type.eq("movie") }
    }

    fun selectSerieRecords(userId: String): Query {
        return selectRecords(userId)
            .andWhere { type.eq("serie") }
    }

    fun selectSerieRecordsAfter(userId: String, time: Int): Query {
        return selectSerieRecords(userId)
            .andWhere { played.greater(timestampToLocalDateTime(time)) }
    }

    fun selectMovieRecordsAfter(userId: String, time: Int): Query {
        return selectMovieRecords(userId)
            .andWhere { played.greater(timestampToLocalDateTime(time)) }
    }

    fun selectMovieRecordOnTitle(userId: String, movieTitle: String): Query {
        return selectMovieRecords(userId)
            .andWhere { title.eq(movieTitle) }
    }

    fun selectSerieRecordOnCollection(userId: String, collection: String): Query {
        return selectSerieRecords(userId)
            .andWhere { ProgressTable.collection.eq(collection) }
            .orWhere { type.eq(collection) }
    }

    fun upsertMovieRecord(
        userId: String,
        title: String,
        collection: String,
        progress: Int,
        duration: Int,
        played: Int,
        videoFile: String
    ): UpsertResult {
        val trimmedTitle = title.trim()
        val playedTime = timestampToLocalDateTime(played)

        val insert = ProgressTable.insertIgnore {
            it[this.guid] = userId
            it[this.type] = "movie"
            it[this.title] = trimmedTitle
            it[this.progress] = progress
            it[this.duration] = duration
            it[this.collection] = collection
            it[this.played] = playedTime
            if (videoFile.isNotBlank()) {
                it[this.video] = videoFile
            }
        }

        return if (insert.insertedCount > 0) {
            UpsertResult.Inserted(insert)
        } else {
            val updatedRows = ProgressTable.update({
                (guid eq userId) and
                        (ProgressTable.title eq trimmedTitle) and
                        (type eq "movie")
            }) {
                it[this.progress] = progress
                it[this.duration] = duration
                it[this.played] = playedTime
                if (videoFile.isNotBlank()) {
                    it[this.video] = videoFile
                }
            }

            if (updatedRows > 0) UpsertResult.Updated else UpsertResult.Skipped
        }
    }

    fun upsertSerieRecord(
        userId: String,
        title: String,
        episode: Int,
        season: Int,
        collection: String,
        progress: Int,
        duration: Int,
        played: Int,
        videoFile: String
    ): UpsertResult {
        val trimmedTitle = title.trim()
        val playedTime = timestampToLocalDateTime(played)

        val insert = ProgressTable.insertIgnore {
            it[this.guid] = userId
            it[this.type] = "serie"
            it[this.title] = trimmedTitle
            it[this.episode] = episode
            it[this.season] = season
            it[this.progress] = progress
            it[this.duration] = duration
            it[this.collection] = collection
            it[this.played] = playedTime
            if (videoFile.isNotBlank()) {
                it[this.video] = videoFile
            }
        }

        return if (insert.insertedCount > 0) {
            UpsertResult.Inserted(insert)
        } else {
            val updatedRows = ProgressTable.update({
                (guid eq userId) and
                        (ProgressTable.title eq trimmedTitle) and
                        (type eq "serie")
            }) {
                it[this.progress] = progress
                it[this.duration] = duration
                it[this.played] = playedTime
                if (videoFile.isNotBlank()) {
                    it[this.video] = videoFile
                }
            }

            if (updatedRows > 0) UpsertResult.Updated else UpsertResult.Skipped
        }
    }

    fun selectResumeEpisode(guid: String, limit: Int): Query {
        val cutoffSeconds = 5

        return ProgressTable
            .join(
                SerieTable, JoinType.INNER,
                onColumn = ProgressTable.collection, otherColumn = SerieTable.collection
            )
            .join(
                ContinueWatchTable, JoinType.LEFT,
                onColumn = SerieTable.collection, otherColumn = ContinueWatchTable.collection
            ) {
                (ContinueWatchTable.userId eq guid) and
                        (ContinueWatchTable.type eq "serie")
            }
            .join(
                CatalogTable, JoinType.INNER,
                onColumn = SerieTable.collection, otherColumn = CatalogTable.collection
            )
            .select(
                ProgressTable.played,
                ProgressTable.progress,
                ProgressTable.duration,
                *SerieTable.columns.toTypedArray()
            )
            .where {
                (ProgressTable.guid eq guid) and
                        (ProgressTable.type eq "serie") and
                        (ProgressTable.progress less (ProgressTable.duration - cutoffSeconds)) and
                        (ProgressTable.episode eq SerieTable.episode) and
                        (ProgressTable.season eq SerieTable.season) and
                        ((ContinueWatchTable.hide eq false) or ContinueWatchTable.hide.isNull())
            }
            .orderBy(ProgressTable.played, SortOrder.DESC)
            .limit(limit)
    }


    private fun internalGetCompletedEpisodes(guid: String, limit: Int, cutoff: Int): List<InternalProgressData> =
        transaction {
            val query = """
            SELECT id, guid, collection, season, episode, progress, duration, played, video, title
            FROM (
                SELECT *,
                       ROW_NUMBER() OVER (
                           PARTITION BY guid, collection
                           ORDER BY season DESC, episode DESC
                       ) AS rn
                FROM progress
                WHERE guid = ?
                  AND episode IS NOT NULL
                  AND season IS NOT NULL
                  AND type = 'serie'
            ) ranked
            WHERE rn = 1
              AND progress > (duration - ?)
              ORDER BY played DESC
            LIMIT ?;
        """.trimIndent()

            exec(
                query,
                args = listOf(
                    VarCharColumnType() to guid,
                    IntegerColumnType() to cutoff,
                    IntegerColumnType() to limit
                )
            ) { rs ->
                val internalTable = mutableListOf<InternalProgressData>()
                while (rs.next()) {
                    internalTable += InternalProgressData(
                        collection = rs.getString("collection"),
                        season = rs.getInt("season"),
                        episode = rs.getInt("episode"),
                    )
                }
                internalTable
            } ?: emptyList()
        }


    fun selectNextEpisodesWithCatalog(guid: String, limit: Int): List<Pair<InternalSerieTableDto, InternalCatalogTableDto>> {
        val cutoffSeconds = 5  // Eksempel: vi definerer en cutoff for progress.
        val completedEpisodes = internalGetCompletedEpisodes(guid, limit, cutoffSeconds)

        return completedEpisodes.mapNotNull { completedEpisode ->
            transaction {
                // Sett opp join-spørringen med en additionalConstraint som joiner på collection.
                val joinQuery: Query = SerieTable.join(
                    otherTable = CatalogTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = { SerieTable.collection eq CatalogTable.collection }
                )
                    .selectAll()
                    .andWhere {
                    (SerieTable.collection eq completedEpisode.collection) and
                            (
                                    (SerieTable.season greater completedEpisode.season) or
                                            (
                                                    (SerieTable.season eq completedEpisode.season) and
                                                            (SerieTable.episode greater completedEpisode.episode)
                                                    )
                                    )
                }
                    .orderBy(SerieTable.season, SortOrder.ASC)
                    .orderBy(SerieTable.episode, SortOrder.ASC)
                    .limit(1)

                // Hent første rad (dersom den finnes) og mapper den til våre data-klasser.
                joinQuery.firstOrNull()?.let { row ->
                    val serie = InternalSerieTableDto(
                        title = row[SerieTable.title],
                        episode = row[SerieTable.episode],
                        season = row[SerieTable.season],
                        collection = row[SerieTable.collection],
                        video = row[SerieTable.video],
                        added = row[SerieTable.added]
                    )
                    val catalog = InternalCatalogTableDto(
                        title = row[CatalogTable.title],
                        cover = row[CatalogTable.cover],
                        type = row[CatalogTable.type],
                        collection = row[CatalogTable.collection],
                        iid = row[CatalogTable.iid],
                        genres = row[CatalogTable.genres],
                        added = row[CatalogTable.added]
                    )
                    serie to catalog
                }
            }
        }
    }

    data class InternalProgressData(
        val collection: String,
        val season: Int,
        val episode: Int,
    )

}
