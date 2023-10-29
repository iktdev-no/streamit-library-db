package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.catalog
import no.iktdev.streamit.library.db.tables.executeWithStatus
import no.iktdev.streamit.library.db.tables.insertWithSuccess
import no.iktdev.streamit.library.db.tables.resumeOrNext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.CurrentDateTime

class ResumeOrNextQuery(
    val userId: String,
    val ignore: Boolean? = null,
    val type: String,
    val collection: String,
    val episode: Int? = null,
    val season: Int? = null,
    val video: String
) : CommonQueryFuncions {

    fun upsertAndGetStatus(): Boolean {
        return executeWithStatus {
            val isPresent = resumeOrNext.select(
                resumeOrNext.collection.eq(collection)
                    .and(resumeOrNext.type.eq(type))
                    .and(resumeOrNext.userId.eq(userId))
            ).singleOrNull() != null

            if (!isPresent) {
                resumeOrNext.insert {
                    it[userId] = this@ResumeOrNextQuery.userId
                    it[type] = this@ResumeOrNextQuery.type
                    if (this@ResumeOrNextQuery.ignore != null) {
                        it[ignore] = this@ResumeOrNextQuery.ignore
                    }
                    it[collection] = this@ResumeOrNextQuery.collection
                    it[episode] = this@ResumeOrNextQuery.episode
                    it[season] = this@ResumeOrNextQuery.season
                    it[video] = this@ResumeOrNextQuery.video
                }
            } else {
                resumeOrNext.update({
                    resumeOrNext.collection.eq(collection)
                        .and(resumeOrNext.type.eq(type))
                        .and(resumeOrNext.userId.eq(userId))
                }) {
                    if (this@ResumeOrNextQuery.ignore != null) {
                        it[ignore] = this@ResumeOrNextQuery.ignore
                    }
                    it[episode] = episode
                    it[season] = season
                    it[video] = video
                    it[updated] = CurrentDateTime
                }
            }
        }
    }
}