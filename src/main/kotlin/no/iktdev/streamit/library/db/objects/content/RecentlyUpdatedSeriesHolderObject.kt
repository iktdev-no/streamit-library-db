package no.iktdev.streamit.library.db.objects.content

import no.iktdev.streamit.library.db.tables.content.CatalogTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import java.time.LocalDateTime

data class RecentlyUpdatedSeriesHolderObject(
    val id: Int,
    val title: String,
    val cover: String?,
    val type: String,
    val collection: String,
    val iid: Int?,
    val genres: String?,
    val added: LocalDateTime,
    val serieTableEntryAdded: LocalDateTime
)

