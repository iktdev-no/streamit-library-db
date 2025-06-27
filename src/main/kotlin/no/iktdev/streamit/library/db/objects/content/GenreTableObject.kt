package no.iktdev.streamit.library.db.objects.content

data class GenreTableObject(
    val id: Int,
    val genre: String
) {
    companion object {
        fun fromRow(row: org.jetbrains.exposed.sql.ResultRow): GenreTableObject {
            return GenreTableObject(
                id = row[no.iktdev.streamit.library.db.tables.content.GenreTable.id].value,
                genre = row[no.iktdev.streamit.library.db.tables.content.GenreTable.genre]
            )
        }
    }
}