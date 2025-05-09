package no.iktdev.streamit.library.db.tables.content

import no.iktdev.streamit.library.db.ext.UpsertResult
import no.iktdev.streamit.library.db.tables.Shared
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update

object ContinueWatchTable: IntIdTable(name = "continueWatch") {
    val userId: Column<String> = varchar("userId", Shared.userIdLength)
    val type: Column<String> = varchar("type", 10)
    val title: Column<String> = varchar("title", 250)
    val collection: Column<String?> = varchar("collection", 250).nullable()
    val hide: Column<Boolean> = bool("hide").default(false)


    fun updateVisibility(userId: String, type: String, collection: String, hide: Boolean = false): UpsertResult {
        val update = ContinueWatchTable.update({
            this@ContinueWatchTable.collection.eq(collection)
                .and(ContinueWatchTable.type.eq(type))
                .and(ContinueWatchTable.userId.eq(userId))
                .and(ContinueWatchTable.collection.eq(collection))
        }) {
            it[this.hide] = hide
        }
        return if (update > 0) UpsertResult.Updated else UpsertResult.Skipped
    }
}