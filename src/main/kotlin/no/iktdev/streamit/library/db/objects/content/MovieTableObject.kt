package no.iktdev.streamit.library.db.objects.content

import no.iktdev.streamit.library.db.tables.content.MovieTable
import org.jetbrains.exposed.sql.ResultRow

data class MovieTableObject(
    val id: Int,
    val video: String
) {
    companion object {
        fun fromRow(row: ResultRow): MovieTableObject {
            return MovieTableObject(
                id = row[MovieTable.id].value,
                video = row[MovieTable.video]
            )
        }
    }
}