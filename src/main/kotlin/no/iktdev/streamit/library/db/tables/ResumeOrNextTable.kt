package no.iktdev.streamit.library.db.tables

import no.iktdev.streamit.library.db.ext.UpsertResult
import no.iktdev.streamit.library.db.tables.content.CatalogTable
import no.iktdev.streamit.library.db.tables.content.ProgressTable
import no.iktdev.streamit.library.db.tables.content.SerieTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

@Deprecated("use query from ProgressTable")
object ResumeOrNextTable : IntIdTable(name = "resumeOrNext") {
    val userId: Column<String> = varchar("userId", Shared.userIdLength)
    val ignore: Column<Boolean> = bool("ignore").default(false)

    val type: Column<String> = varchar("type", 10)
    val collection: Column<String> = varchar("collection", 250)
    val episode: Column<Int?> = integer("episode").nullable()
    val season: Column<Int?> = integer("season").nullable()
    val video: Column<String> = varchar("video", 500)
    val updated: Column<LocalDateTime> = datetime("played").defaultExpression(CurrentDateTime)

    init {
        uniqueIndex(collection, type, userId)
    }


    fun upsert(userId: String, type: String, collection: String, episode: Int? = null, season: Int? = null, video: String, updated: LocalDateTime = LocalDateTime.now(), hide: Boolean = false): UpsertResult {

        val insert = ResumeOrNextTable.insertIgnore {
            it[this.userId] = userId
            it[this.type] = type
            it[this.ignore] = hide
            it[this.collection] = collection
            it[this.episode] = episode
            it[this.season] = season
            it[this.video] = video
            it[this.updated] = updated
        }

        return if (insert.insertedCount > 0) {
            UpsertResult.Inserted(insert)
        } else {
            val update = ResumeOrNextTable.update({
                this@ResumeOrNextTable.collection.eq(collection)
                    .and(ResumeOrNextTable.type.eq(type))
                    .and(ResumeOrNextTable.userId.eq(userId))
            }) {
                it[this.episode] = episode
                it[this.season] = season
                it[this.video] = video
                it[this.updated] = updated
            }
            if (update > 0) UpsertResult.Updated else UpsertResult.Skipped
        }
    }

    fun updateVisibility(userId: String, type: String, collection: String, episode: Int? = null, season: Int? = null, video: String, hide: Boolean = false): UpsertResult {
        val update = ResumeOrNextTable.update({
            this@ResumeOrNextTable.collection.eq(collection)
                .and(ResumeOrNextTable.type.eq(type))
                .and(ResumeOrNextTable.userId.eq(userId))
                .and(ResumeOrNextTable.collection.eq(collection))
                .and(ResumeOrNextTable.episode.eq(episode))
                .and(ResumeOrNextTable.season.eq(season))
                .and(ResumeOrNextTable.video.eq(video))
        }) {
            it[this.ignore] = hide
        }
        return if (update > 0) UpsertResult.Updated else UpsertResult.Skipped
    }

    fun selectResumeOrNext(limit: Int): Query {
        return ResumeOrNextTable
            .join(ProgressTable, JoinType.INNER) {
                ProgressTable.episode.eq(episode)
                    .and(ProgressTable.season.eq(season))
                    .and(ProgressTable.collection.eq(collection))
                    .and(ProgressTable.guid.eq(userId))
            }
            .join(SerieTable, JoinType.INNER) {
                SerieTable.collection.eq(collection)
                    .and(SerieTable.episode.eq(episode))
                    .and(SerieTable.season.eq(season))
            }
            .join(CatalogTable, JoinType.INNER) {
                CatalogTable.collection.eq(collection)
                    .and(CatalogTable.type.eq(type))
            }
            .selectAll()
            .where { ignore.neq(true) }
            .andWhere { userId.eq(userId) }
            .andWhere { type.eq("serie") }
            .orderBy(updated, SortOrder.DESC)
            .limit(limit)
    }


}