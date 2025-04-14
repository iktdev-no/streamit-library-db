package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.insertWithSuccess
import no.iktdev.streamit.library.db.tables.serie
import org.jetbrains.exposed.sql.insertIgnore

class SerieQuery(
    val title: String?,
    val episode: Int,
    val season: Int,
    val collection: String,
    val video: String
): BaseQuery() {

    override fun insert() {
        insertAndGetStatus()
    }

    override fun insertAndGetStatus(): Boolean {
        return insertWithSuccess(run = {
            serie.insertIgnore {
                it[title] = this@SerieQuery.title
                it[episode] = this@SerieQuery.episode
                it[season] = this@SerieQuery.season
                it[collection] = this@SerieQuery.collection
                it[video] = this@SerieQuery.video
            }
        })
    }


}