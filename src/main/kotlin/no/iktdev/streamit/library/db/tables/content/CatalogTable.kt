package no.iktdev.streamit.library.db.tables.content

import no.iktdev.streamit.library.db.objects.content.CatalogTableObject
import no.iktdev.streamit.library.db.objects.content.RecentlyUpdatedSeriesHolderObject
import no.iktdev.streamit.library.db.withTransaction
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object CatalogTable : IntIdTable(name = "catalog") {
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
    fun selectOnlyMovies(database: Database? = null, onError: ((Exception) -> Unit)? = null): List<CatalogTableObject> = withTransaction(database, onError) {
        CatalogTable
            .selectAll()
            .where { type eq "movie" }
            .andWhere { collection.isNotNull() }
            .map { CatalogTableObject.fromRow(it) }
    } ?: emptyList()


    fun selectOnlySeries(database: Database? = null, onError: ((Exception) -> Unit)? = null): List<CatalogTableObject> = withTransaction(database, onError) {
        CatalogTable.selectAll()
            .where { type eq "serie" }
            .andWhere { collection.isNotNull() }
            .map { CatalogTableObject.fromRow(it) }
    } ?: emptyList()

    fun selectWhereGenreIsSet(database: Database? = null, onError: ((Exception) -> Unit)? = null): List<CatalogTableObject> = withTransaction(database, onError) {
        CatalogTable.selectAll()
            .where {genres.isNotNull() }
            .map { CatalogTableObject.fromRow(it) }
    } ?: emptyList()

    fun selectRecentlyAdded(limit: Int, database: Database? = null, onError: ((Exception) -> Unit)? = null): List<CatalogTableObject> = withTransaction(database, onError) {
        CatalogTable.selectAll()
            .orderBy(id, SortOrder.DESC)
            .limit(limit)
            .map { CatalogTableObject.fromRow(it) }
    } ?: emptyList()

    /**
     * @param noOlderThan Defines the final cutoff for content to be shown
     */
    fun selectNewlyUpdatedSeries(noOlderThan: LocalDateTime, database: Database? = null, onError: ((Exception) -> Unit)? = null): List<RecentlyUpdatedSeriesHolderObject> = withTransaction(database, onError) {
        val episodeAddedAlias = SerieTable.added.max().alias("episodeAdded")
        val latestEpisode = SerieTable
            .select(SerieTable.collection, episodeAddedAlias)
            .groupBy(SerieTable.collection)
            .alias("UpdatedEpisodeTable")

        CatalogTable
            .join(latestEpisode, JoinType.INNER) {
                CatalogTable.collection eq latestEpisode[SerieTable.collection]
            }
            .selectAll()
            .where { CatalogTable.type eq "serie" }
            .andWhere { latestEpisode[episodeAddedAlias].greater(noOlderThan) }
            .orderBy(latestEpisode[episodeAddedAlias], SortOrder.DESC)
            .map { row ->
                RecentlyUpdatedSeriesHolderObject(
                    id = row[id].value,
                    title = row[title],
                    cover = row[cover],
                    type = row[type],
                    collection = row[collection],
                    iid = row[iid],
                    genres = row[genres],
                    added = row[added],
                    serieTableEntryAdded = row[latestEpisode[episodeAddedAlias]] ?: LocalDateTime.MIN
                )
            }
    } ?: emptyList()


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


    fun searchMovieWith(keyword: String, database: Database? = null, onError: ((Exception) -> Unit)? = null): List<CatalogTableObject> = withTransaction(database, onError) {
        CatalogTable
            .selectAll()
            .where { type eq "movie" }
            .andWhere { collection.isNotNull() }
            .andWhere { this@CatalogTable.title like "$keyword%" }
            .orWhere { this@CatalogTable.collection like "%$keyword%" }
            .map { CatalogTableObject.fromRow(it) }
    } ?: emptyList()

    fun searchSerieWith(keyword: String, database: Database? = null, onError: ((Exception) -> Unit)? = null): List<CatalogTableObject> = withTransaction(database, onError) {
        CatalogTable.selectAll()
            .where { type eq "serie" }
            .andWhere { collection.isNotNull() }
            .andWhere { this@CatalogTable.title like "$keyword%" }
            .orWhere { this@CatalogTable.collection like "%$keyword%" }
            .map { CatalogTableObject.fromRow(it) }
    } ?: emptyList()

    fun searchWith(keyword: String, database: Database? = null, onError: ((Exception) -> Unit)? = null): List<CatalogTableObject> = withTransaction(database, onError) {
        CatalogTable.selectAll()
            .where { this@CatalogTable.title like "$keyword%" }
            .orWhere { this@CatalogTable.collection like "%$keyword%" }
            .map { CatalogTableObject.fromRow(it) }
    } ?: emptyList()


}