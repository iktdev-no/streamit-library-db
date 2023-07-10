package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.TableDefaultOperations
import no.iktdev.streamit.library.db.tables.movie
import no.iktdev.streamit.library.db.tables.withTransaction
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

class MovieQuery(val videoFile: String): BaseQuery() {
    override fun insertAndGetId(): Int? {
        return withTransaction {
            movie.insertAndGetId {
                it[video] = videoFile
            }.value
        }
    }


}