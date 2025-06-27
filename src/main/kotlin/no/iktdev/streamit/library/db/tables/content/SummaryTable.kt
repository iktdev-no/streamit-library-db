package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.statements.InsertStatement

object SummaryTable : IntIdTable(name = "summary") {
    val description: Column<String> = text("description")
    val language: Column<String> = varchar("language", 16)
    val cid: Column<Int> = integer("cid")

    init {
        uniqueIndex(language, cid)
    }

    fun insertIgnore(catalogId: Int, language: String, content: String): InsertStatement<Long> {
        return SummaryTable.insertIgnore {
            it[cid] = catalogId
            it[SummaryTable.language] = language
            it[description] = content
        }
    }
}