package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

object FavoriteTable: IntIdTable(name = "Favorites") {
    val userId: Column<String> = varchar("userId", 50)
    val catalogId: Column<Int> = integer("catalogId")
    val title = text("catalogTitle").nullable()

    init {
        uniqueIndex(userId, catalogId)
    }

    fun addFavorite(userId: String, catalogId: Int, title: String?): Boolean = transaction {
        FavoriteTable.insertIgnore {
            it[FavoriteTable.userId] = userId
            it[FavoriteTable.catalogId] = catalogId
            it[FavoriteTable.title] = title
        }.insertedCount != 0
    }

    fun removeFavorite(userId: String, catalogId: Int): Boolean = transaction {
        FavoriteTable.deleteWhere { (FavoriteTable.userId eq userId) and (FavoriteTable.catalogId eq catalogId) } != 0
    }

    fun getFavorites(userId: String): List<Int> = transaction {
        FavoriteTable.select(FavoriteTable.catalogId).where { (FavoriteTable.userId eq userId) }
            .map { it -> it[FavoriteTable.catalogId] }
    }

}