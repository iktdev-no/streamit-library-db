package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.insertWithSuccess
import no.iktdev.streamit.library.db.tables.summary
import org.jetbrains.exposed.sql.insertIgnore

class SummaryQuery(
    val cid: Int,
    val language: String,
    val description: String
): BaseQuery() {
    override fun insertAndGetStatus(): Boolean {
        return insertWithSuccess(block = {
            summary.insertIgnore {
                it[cid] = this@SummaryQuery.cid
                it[language] = this@SummaryQuery.language
                it[description] = this@SummaryQuery.description
            }
        })
    }

    override fun insert() {
        insertAndGetStatus()
    }
}