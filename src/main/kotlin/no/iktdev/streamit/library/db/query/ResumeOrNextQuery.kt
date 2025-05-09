package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.executeWithStatus
import no.iktdev.streamit.library.db.tables.ResumeOrNextTable
import no.iktdev.streamit.library.db.withTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

@Deprecated("")
class ResumeOrNextQuery(
    val userId: String,
    val ignore: Boolean? = null,
    val type: String,
    val collection: String,
    val episode: Int? = null,
    val season: Int? = null,
    val video: String,
    val updated: LocalDateTime? = null
)  {

    fun upsertAndGetStatus(onError: ((Exception) -> Unit)? = null): Boolean {
        val isPresent = withTransaction (run = {
            ResumeOrNextTable.select(
                ResumeOrNextTable.collection.eq(collection)
                    .and(ResumeOrNextTable.type.eq(type))
                    .and(ResumeOrNextTable.userId.eq(userId))
            ).singleOrNull()
        }, onError = onError) != null

        return if (!isPresent) {
            executeWithStatus (run = {
                ResumeOrNextTable.insert {
                    it[userId] = this@ResumeOrNextQuery.userId
                    it[type] = this@ResumeOrNextQuery.type
                    if (this@ResumeOrNextQuery.ignore != null) {
                        it[ignore] = this@ResumeOrNextQuery.ignore
                    }
                    it[collection] = this@ResumeOrNextQuery.collection
                    it[episode] = this@ResumeOrNextQuery.episode
                    it[season] = this@ResumeOrNextQuery.season
                    it[video] = this@ResumeOrNextQuery.video
                    it[updated] = this@ResumeOrNextQuery.updated ?: LocalDateTime.now()
                }
            }, onError = onError)
        } else {
            executeWithStatus (run = {
                ResumeOrNextTable.update({
                    ResumeOrNextTable.collection.eq(collection)
                        .and(ResumeOrNextTable.type.eq(type))
                        .and(ResumeOrNextTable.userId.eq(userId))
                }) {
                    if (this@ResumeOrNextQuery.ignore != null) {
                        it[ignore] = this@ResumeOrNextQuery.ignore
                    }
                    it[episode] = episode
                    it[season] = season
                    it[video] = video
                    it[updated] = this@ResumeOrNextQuery.updated ?: LocalDateTime.now()
                }
            }, onError = onError)
        }


    }
}