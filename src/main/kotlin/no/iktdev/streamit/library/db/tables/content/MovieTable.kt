package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object MovieTable: IntIdTable(name = "Movie") {
    val video: Column<String> = varchar("video", 250).uniqueIndex()

    fun insertAndGetId(videoFile: String): EntityID<Int>? {
        return MovieTable.insertIgnoreAndGetId {
            it[video] = videoFile
        }
    }

    fun selectOnId(id: Int): Query {
        return CatalogTable.innerJoin(MovieTable, { iid }, { MovieTable.id })
            .selectAll()
            .where { CatalogTable.id eq id }
            .andWhere { CatalogTable.iid.isNotNull() }
    }


    fun insertMovie(videoFile: String): EntityID<Int>? {
        return MovieTable.insertIgnoreAndGetId {
            it[video] = videoFile
        }
    }
}