package no.iktdev.streamit.library.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object registeredDevices : IntIdTable() {
    val deviceId: Column<String> = varchar("deviceId", 256)
    val applicationPackageName: Column<String> = varchar("applicationPackageName", 32)
    val osVersion: Column<String> = varchar("osVersion", 28)
    val osPlatform: Column<String> = varchar("osPlatform", 28)
    val registered: Column<LocalDateTime> = datetime("registered").defaultExpression(CurrentDateTime)
    val lastSeen: Column<LocalDateTime> = datetime("lastSeen").defaultExpression(CurrentDateTime)
}