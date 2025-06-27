package no.iktdev.streamit.library.db.objects

import org.jetbrains.exposed.sql.Column

data class TitleTableObject(
    val masterTitle: String,
    val alternativeTitle: String
)
