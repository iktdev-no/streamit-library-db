package no.iktdev.streamit.library.db.tables.content

import no.iktdev.streamit.library.db.objects.content.MovieTableObject
import no.iktdev.streamit.library.db.withTransaction
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object MovieTable: IntIdTable(name = "movie") {
    val video: Column<String> = varchar("video", 250).uniqueIndex()

    fun insertAndGetId(videoFile: String): EntityID<Int>? {
        return MovieTable.insertIgnoreAndGetId {
            it[video] = videoFile
        }
    }

    fun selectOnId(id: Int, database: Database? = null, onError: ((Exception) -> Unit)? = null): MovieTableObject? = withTransaction(database, onError) {
        MovieTable
            .selectAll()
            .where { MovieTable.id eq id }
            .map { MovieTableObject.fromRow(it) }
            .firstOrNull()
    }


    fun insertMovie(videoFile: String): EntityID<Int>? {
        return MovieTable.insertIgnoreAndGetId {
            it[video] = videoFile
        }
    }
}