package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.catalog
import no.iktdev.streamit.library.db.tables.withTransaction
import org.jetbrains.exposed.sql.insertAndGetId

class CatalogQuery(
    val title: String,
    val cover: String?,
    val type: String,
    val collection: String,
    var iid: Int? = null,
    val genres: String? = null
) : BaseQuery() {
    override fun insert() {
        withTransaction {
            catalog.insertAndGetId {
                it[title] = this@CatalogQuery.title
                it[cover] = this@CatalogQuery.cover
                it[type] = this@CatalogQuery.type
                it[collection] = this@CatalogQuery.collection
                it[iid] = this@CatalogQuery.iid
                it[genres] = this@CatalogQuery.genres
            }
        }
    }

    fun insertWithMovie(videoFile: String): Boolean {
        iid = MovieQuery(videoFile).insertAndGetId()
        return if (iid != null) {
            insert()
            true
        } else false
    }

    fun insertWithSerie(episodeTitle: String, episode: Int, season: Int, videoFile: String): Boolean {
        val success = SerieQuery(
            title = episodeTitle,
            episode = episode,
            season = season,
            collection = this@CatalogQuery.collection,
            video = videoFile
        ).insertAndGetStatus()
        if (success) {
            insert()
        }
        return success
    }
}