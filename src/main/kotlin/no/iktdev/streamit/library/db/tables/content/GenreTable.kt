package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

object GenreTable: IntIdTable(name = "Genre") {
    val genre: Column<String> = varchar("genre", 50).uniqueIndex()

    fun selectById(id: Int): Query {
        return GenreTable.selectAll()
            .where{ GenreTable.id.eq(id) }
    }

    fun selectByIds(ids: List<Int>): Query {
        return GenreTable.selectAll()
            .where { id inList ids }
    }
}