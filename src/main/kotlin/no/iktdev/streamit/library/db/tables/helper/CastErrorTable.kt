package no.iktdev.streamit.library.db.tables.helper

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object cast_errors : IntIdTable() {
    val file: Column<String> = varchar("source", 200)
    val deviceModel: Column<String> = varchar("deviceModel", 50)
    val deviceManufacturer: Column<String> = varchar("deviceManufacturer", 50)
    val deviceBrand: Column<String> = varchar("deviceBrand", 50)
    val deviceAndroidVersion = varchar("androidVersion", 10)
    val appVersion = varchar("appVersion", 10)
    val castDeviceName: Column<String> = varchar("castDeviceName", 50)
    val error = text("error")
    val timestamp = datetime("timestamp").clientDefault { LocalDateTime.now() }
}