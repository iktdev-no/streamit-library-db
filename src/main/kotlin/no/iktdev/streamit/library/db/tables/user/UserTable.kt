package no.iktdev.streamit.library.db.tables.user

import no.iktdev.streamit.library.db.ext.UpsertResult
import org.jetbrains.exposed.sql.*

object UserTable : Table(name = "users") {
    val guid: Column<String> = varchar("guid", 50)
    val name: Column<String> = varchar("name", 50).uniqueIndex()
    val image: Column<String> = varchar("image", 200)

    fun selectUser(userId: String): Query {
        return UserTable.selectAll().where { guid eq userId }
    }

    fun upsert(userId: String, name: String, image: String): UpsertResult {
        val insert = UserTable.insertIgnore {
            it[this.guid] = userId
            it[this.name] = name
            it[this.image] = image
        }

        return if (insert.insertedCount > 0) {
            UpsertResult.Inserted(insert)
        } else {
            val update = UserTable.update({
                (guid eq userId)
            }) {
                it[this.name] = name
                it[this.image] = name
            }
            if (update > 0) UpsertResult.Updated else UpsertResult.Skipped

        }
    }
}