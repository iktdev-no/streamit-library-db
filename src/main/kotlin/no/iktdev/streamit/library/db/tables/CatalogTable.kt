package no.iktdev.streamit.library.db.tables

import no.iktdev.streamit.library.db.tables.progress.clientDefault
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object catalog : IntIdTable() {
    val title: Column<String> = varchar("title", 250)
    var cover: Column<String?> = varchar("cover", 250).nullable()
    var type: Column<String> = varchar("type", 50)
    var collection: Column<String> = varchar("collection", 100)
    var iid: Column<Int?> = integer("iid").nullable()
    var genres: Column<String?> = varchar("genres", 24).nullable()
    val added: Column<Instant> = timestamp("added").clientDefault { Instant.now() }

    init {
        uniqueIndex(title, type)
    }
}