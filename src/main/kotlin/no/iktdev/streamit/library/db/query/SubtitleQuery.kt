package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.insertWithSuccess
import no.iktdev.streamit.library.db.tables.subtitle
import org.jetbrains.exposed.sql.insert

class SubtitleQuery(
    val title: String,
    val language: String = "eng",
    val collection: String,
    val format: String
): BaseQuery() {
    override fun insertAndGetStatus(): Boolean {
        return insertWithSuccess {
            subtitle.insert {
                it[title] = this@SubtitleQuery.title
                it[language] = this@SubtitleQuery.language
                it[collection] = this@SubtitleQuery.collection
                it[format] = this@SubtitleQuery.format
            }
        }
    }
}