package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.time.LocalDateTime

object SerieTable: IntIdTable(name = "Serie") {
    val title: Column<String?> = varchar("title", 250).nullable()
    val episode: Column<Int> = integer("episode")
    val season: Column<Int> = integer("season")
    val collection: Column<String> = varchar("collection", 250)
    val video: Column<String> = varchar("video", 500).uniqueIndex()
    val added: Column<LocalDateTime> = datetime("added").defaultExpression(CurrentDateTime)

    init {
        uniqueIndex(episode, season, collection)
    }

    fun insertIgnore(title: String, collection: String, episode: Int, season: Int, videoFile: String): InsertStatement<Long> {
        return SerieTable.insertIgnore {
            it[SerieTable.title] = title
            it[SerieTable.episode] = episode
            it[SerieTable.season] = season
            it[SerieTable.collection] = collection
            it[video] = videoFile
        }
    }

    fun insertSerie(title: String, collection: String, episode: Int, season: Int, videoFile: String): EntityID<Int>? {
        return SerieTable.insertIgnoreAndGetId {
            it[this.title] = title
            it[this.collection] = collection
            it[this.episode] = episode
            it[this.season] = season
            it[this.video] = videoFile
            it[this.added] = LocalDateTime.now()
        }
    }

    fun selectOnCollection(collection: String): Query {
        return CatalogTable.join(SerieTable, JoinType.INNER) {
            CatalogTable.collection eq SerieTable.collection
        }
            .selectAll()
            .where { CatalogTable.collection eq collection }
            .orderBy(season)
            .orderBy(episode)
            .andWhere { CatalogTable.collection.isNotNull() }
            .andWhere { CatalogTable.type.eq("serie") }
    }

    fun selectOnId(id: Int): Query {
        return CatalogTable.join(SerieTable, JoinType.INNER) {
            CatalogTable.collection eq SerieTable.collection
        }
            .selectAll()
            .where { CatalogTable.id eq id }
            .orderBy(season)
            .orderBy(episode)
            .andWhere { CatalogTable.collection.isNotNull() }
            .andWhere { CatalogTable.type.eq("serie") }
    }
}
