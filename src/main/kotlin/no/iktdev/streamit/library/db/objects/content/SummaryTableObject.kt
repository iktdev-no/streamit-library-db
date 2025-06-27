package no.iktdev.streamit.library.db.objects

import org.jetbrains.exposed.sql.Column

data class SummaryTableObject(
    val description: String,
    val language: String,
    val cid: Int
)

