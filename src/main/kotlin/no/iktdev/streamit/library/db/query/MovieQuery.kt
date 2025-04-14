package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.movie
import no.iktdev.streamit.library.db.withTransaction
import org.jetbrains.exposed.sql.insertIgnoreAndGetId

class MovieQuery(val videoFile: String): BaseQuery() {
    override fun insertAndGetId(): Int? {
        return withTransaction(run =  {
            movie.insertIgnoreAndGetId {
                it[video] = videoFile
            }?.value
        })
    }

    override fun insert() {
        insertAndGetId()
    }

}