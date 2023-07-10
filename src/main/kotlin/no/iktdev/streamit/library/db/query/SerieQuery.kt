package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.insertWithSuccess
import no.iktdev.streamit.library.db.tables.serie
import no.iktdev.streamit.library.db.tables.withTransaction
import org.jetbrains.exposed.sql.insert

class SerieQuery(
    val title: String,
    val episode: Int,
    val season: Int,
    val collection: String,
    val video: String
): BaseQuery() {

    override fun insertAndGetStatus(): Boolean {
        return insertWithSuccess {
            serie.insert {
                it[title] = this@SerieQuery.title
                it[episode] = this@SerieQuery.episode
                it[season] = this@SerieQuery.season
                it[collection] = this@SerieQuery.collection
                it[video] = this@SerieQuery.video
            }
        }
    }


}