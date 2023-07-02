package no.iktdev.library.db.datasource

import org.jetbrains.exposed.sql.Database

open class DataSource(val connectionUrl: String, val databaseName: String, val address: String, val port: String?, val username: String, val password: String) {

    open fun createDatabase(): Database? {
        return toDatabase()
    }

    fun toPortedAddress(): String {
        return if (!address.contains(":") && port?.isNullOrBlank() != true) {
            "$address:$port"
        } else address
    }

     protected fun toDatabaseServerConnection(): Database {
        return Database.connect(
            "$connectionUrl://${toPortedAddress()}",
            user = username,
            password = password
        )
    }

    fun toDatabase(): Database {
        return Database.connect(
            "$connectionUrl://${toPortedAddress()}/$databaseName",
            user = username,
            password = password
        )
    }

}
