package no.iktdev.streamit.library.db.query

import no.iktdev.streamit.library.db.tables.genre
import org.jetbrains.exposed.sql.insertIgnoreAndGetId
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select

class GenreQuery(
    vararg val genres: String
) {
    fun insertAndGetIds(): Map<String, Int?> {
        return genres.associateWith { genreName ->
            genre.insertIgnoreAndGetId {
                it[genre] = genreName
            }?.value
        }
    }

    /**
     * @return List of ids found using {vararg val genres: String in class}
     */
    fun getIds(): List<Int> {
        return genre.select {
            genre.genre inList genres.toList()
        }.map { it[genre.id].value }
    }
}