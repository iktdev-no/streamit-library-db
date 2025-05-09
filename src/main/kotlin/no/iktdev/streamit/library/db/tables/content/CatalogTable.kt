package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.time.LocalDateTime

object CatalogTable : IntIdTable(name = "Catalog") {
    val title: Column<String> = varchar("title", 250)
    var cover: Column<String?> = varchar("cover", 250).nullable()
    var type: Column<String> = varchar("type", 50)
    var collection: Column<String> = varchar("collection", 250)
    var iid: Column<Int?> = integer("iid").nullable()
    var genres: Column<String?> = varchar("genres", 24).nullable()
    val added: Column<LocalDateTime> = datetime("added").defaultExpression(CurrentDateTime)

    init {
        uniqueIndex(title, type)
    }

    fun selectOnlyMovies(): Query {
        return CatalogTable.select(type eq "movie")
            .andWhere { iid.isNotNull() }
            .andWhere { iid.greater(0) }
    }

    fun selectOnlySeries(): Query {
        return CatalogTable.select(type eq "serie")
            .andWhere { collection.isNotNull() }
    }

    fun selectWhereGenreIsSet(): Query {
        return CatalogTable.select( genres.isNotNull())
    }

    fun selectRecentlyAdded(limit: Int): Query {
        return CatalogTable.selectAll()
            .orderBy(id, SortOrder.DESC)
            .limit(limit)
    }

    /**
     * @param noOlderThan Defines the final cutoff for content to be shown
     */
    fun selectNewlyUpdatedSeries(noOlderThan: LocalDateTime): Query {
        val latestEpisode = SerieTable.select(collection, added.max().alias("episodeAdded"))
            .groupBy(SerieTable.collection).alias("UpdatedEpisodeTable")
        return CatalogTable.join(latestEpisode, JoinType.INNER) {
            CatalogTable.collection eq latestEpisode[SerieTable.collection]
        }
            .selectAll()
            .andWhere { type eq "serie" }
            .andWhere { latestEpisode[SerieTable.added].greater(noOlderThan) }
            .orderBy(latestEpisode[SerieTable.added], SortOrder.DESC)
    }


    fun insertMovie(title: String, collection: String, cover: String? = null, genres: String? = null, videoFile: String): InsertStatement<Number>? {
        val inserted = MovieTable.insertMovie(videoFile)?.value ?: return null

        return CatalogTable.insert {
            it[iid] = inserted
            it[CatalogTable.title] = title
            it[CatalogTable.collection] = collection
            it[CatalogTable.cover] = cover
            it[CatalogTable.type] = "movie"
            it[CatalogTable.genres] = genres
            it[CatalogTable.added] = LocalDateTime.now()
        }

    }

    fun insertSerie(title: String, collection: String, cover: String? = null, genres: String? = null, videoFile: String, episode: Int, season: Int): InsertStatement<Long>? {
        val inserted = SerieTable.insertSerie(title, collection, episode, season, videoFile)?.value ?: return null

        return CatalogTable.insertIgnore {
            it[iid] = inserted
            it[CatalogTable.title] = title
            it[CatalogTable.collection] = collection
            it[CatalogTable.cover] = cover
            it[CatalogTable.type] = "serie"
            it[CatalogTable.genres] = genres
            it[CatalogTable.added] = LocalDateTime.now()
        }

    }


    fun searchMovieWith(keyword: String): Query {
        return selectOnlyMovies()
            .andWhere { this@CatalogTable.title like "$keyword%" }
            .orWhere { this@CatalogTable.collection like "%$keyword%" }
    }

    fun searchSerieWith(keyword: String): Query {
        return selectOnlySeries()
            .andWhere { this@CatalogTable.title like "$keyword%" }
            .orWhere { this@CatalogTable.collection like "%$keyword%" }
    }

    fun searchWith(keyword: String): Query {
        return CatalogTable.select(this@CatalogTable.title like "$keyword%")
            .orWhere { this@CatalogTable.collection like "%$keyword%" }
    }


}