package no.iktdev.streamit.library.db.tables.authentication

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object DelegatedAuthenticationTable: IntIdTable(name = "DelegatedAuthentication") {
    val pin: Column<String> = varchar("pin", 8)
    val requesterId: Column<String> = char("requesterId", 64)
    val deviceInfo: Column<String> = varchar("deviceInfo", 256)
    val created: Column<LocalDateTime> = datetime("created").clientDefault { LocalDateTime.now() }
    val expires: Column<LocalDateTime> = datetime("expires").clientDefault {  LocalDateTime.now().plusMinutes(15) }
    val permitted: Column<Boolean> = bool("permitted").default(false)
    val consumed: Column<Boolean> = bool("consumed").default(false)
    val method = enumerationByName("method", 3, AuthMethod::class) // Brukt metode (PIN eller QR)
    val ipaddress: Column<String?> = varchar("ipaddress", 39).nullable()

    init {
        uniqueIndex(pin)
    }

    enum class AuthMethod {
        PIN, QR
    }
}

