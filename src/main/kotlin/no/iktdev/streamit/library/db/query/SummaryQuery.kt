package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.insertWithSuccess
import no.iktdev.streamit.library.db.tables.summary
import no.iktdev.streamit.library.db.tables.withTransaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore

class SummaryQuery(
    val cid: Int,
    val language: String,
    val description: String
): BaseQuery() {
    override fun insertAndGetStatus(): Boolean {
        return insertWithSuccess {
            summary.insertIgnore {
                it[cid] = this@SummaryQuery.cid
                it[language] = this@SummaryQuery.language
                it[description] = this@SummaryQuery.description
            }
        }
    }

    override fun insert() {
        insertAndGetStatus()
    }
}