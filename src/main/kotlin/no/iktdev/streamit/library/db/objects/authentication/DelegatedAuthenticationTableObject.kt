package no.iktdev.streamit.library.db.objects.authentication

import no.iktdev.streamit.library.db.tables.authentication.DelegatedAuthenticationTable
import java.time.LocalDateTime

data class DelegatedAuthenticationTableObject(
    val pin: String,
    val requesterId: String,
    val deviceInfo: String,
    val created: LocalDateTime,
    val expires: LocalDateTime,
    val permitted: Boolean,
    val consumed: Boolean,
    val method: DelegatedAuthenticationTable.AuthMethod,
    val ipaddress: String?
)