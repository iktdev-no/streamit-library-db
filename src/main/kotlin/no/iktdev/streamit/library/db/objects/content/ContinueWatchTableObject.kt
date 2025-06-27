package no.iktdev.streamit.library.db.objects

import no.iktdev.streamit.library.db.tables.Shared
import org.jetbrains.exposed.sql.Column

data class ContinueWatchTableObject(
    val userId: String,
    val type: String,
    val title: String,
    val collection: String?,
    val hide: Boolean
)