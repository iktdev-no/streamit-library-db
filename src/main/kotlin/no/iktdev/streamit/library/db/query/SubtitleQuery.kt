package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.insertWithSuccess
import no.iktdev.streamit.library.db.tables.subtitle
import org.jetbrains.exposed.sql.insertIgnore

@SuppressWarnings("CanBePrivate")
class SubtitleQuery(
    @SuppressWarnings("CanBePrivate") val associatedWithVideo: String,
    @SuppressWarnings("CanBePrivate") val language: String = "eng",
    @SuppressWarnings("CanBePrivate") val collection: String,
    @SuppressWarnings("CanBePrivate") val file: String,
    @SuppressWarnings("CanBePrivate") val format: String
): BaseQuery() {

    override fun insert() {
        insertAndGetStatus()
    }
    override fun insertAndGetStatus(): Boolean {
        return insertWithSuccess(run =  {
            subtitle.insertIgnore {
                it[associatedWithVideo] = this@SubtitleQuery.associatedWithVideo
                it[language] = this@SubtitleQuery.language
                it[collection] = this@SubtitleQuery.collection
                it[format] = this@SubtitleQuery.format
                it[subtitle] = this@SubtitleQuery.file
            }
        })
    }
}