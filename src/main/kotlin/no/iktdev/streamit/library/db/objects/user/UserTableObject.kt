package no.iktdev.streamit.library.db.objects.user

import org.jetbrains.exposed.sql.Column

data class UserTableObject(
    val guid: String,
    val name: String,
    val image: String
)
