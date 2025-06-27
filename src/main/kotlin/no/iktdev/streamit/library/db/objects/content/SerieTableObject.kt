package no.iktdev.streamit.library.db.objects

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class SerieTableObject(
    val title: String?,
    val episode: Int,
    val season: Int,
    val collection: String,
    val video: String,
    val added: LocalDateTime
)
