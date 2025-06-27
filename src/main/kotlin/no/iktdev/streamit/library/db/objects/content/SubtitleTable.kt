package no.iktdev.streamit.library.db.objects

import org.jetbrains.exposed.sql.Column

data class SubtitleTable(
    val associatedWithVideo: String,
    val language: String,
    val subtitle: String,
    val collection: String,
    val format: String
)
