package no.iktdev.streamit.library.db.tables.other

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.insertIgnoreAndGetId
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import javax.print.attribute.standard.PrinterMoreInfoManufacturer

object CastErrorTable : IntIdTable(name = "CastError") {
    val file: Column<String> = varchar("source", 200)
    val deviceModel: Column<String> = varchar("deviceModel", 50)
    val deviceManufacturer: Column<String> = varchar("deviceManufacturer", 50)
    val deviceBrand: Column<String> = varchar("deviceBrand", 50)
    val deviceOsVersion = varchar("OsVersion", 10)
    val appVersion = varchar("appVersion", 10)
    val castDeviceName: Column<String> = varchar("castDeviceName", 50)
    val error = text("error").nullable()
    val timestamp = datetime("timestamp").clientDefault { LocalDateTime.now() }


    fun insert(deviceOsVersion: String, castDeviceName: String, appVersion: String, file: String, deviceBrand: String, deviceModel: String, deviceManufacturer: String, error: String): EntityID<Int>? {
        return CastErrorTable.insertIgnoreAndGetId {
            it[CastErrorTable.file] = file
            it[CastErrorTable.deviceModel] = deviceModel
            it[CastErrorTable.deviceManufacturer] = deviceManufacturer
            it[CastErrorTable.deviceBrand] = deviceBrand
            it[CastErrorTable.deviceOsVersion] = deviceOsVersion
            it[CastErrorTable.appVersion] = appVersion
            it[CastErrorTable.castDeviceName] = castDeviceName
            it[CastErrorTable.error] = error
        }
    }
}