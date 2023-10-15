package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.catalog
import no.iktdev.streamit.library.db.tables.executeWithStatus
import no.iktdev.streamit.library.db.tables.insertWithSuccess
import no.iktdev.streamit.library.db.tables.resumeOrNext
import org.jetbrains.exposed.sql.upsert

class ResumeOrNextQuery(
    val userId: String,
    val ignore: Boolean = false,
    val type: String,
    val collection: String,
    val episode: Int? = null,
    val season: Int? = null,
    val video: String
): CommonQueryFuncions {

    override fun insertAndGetStatus(): Boolean {
        return executeWithStatus {
            resumeOrNext.upsert(resumeOrNext.collection, resumeOrNext.type, resumeOrNext.userId) {
                it[userId] = userId
                it[ignore] = ignore
                it[type] = type
                it[collection] = collection
                it[episode] = episode
                it[season] = season
                it[video] = video
            }
        }
    }
}