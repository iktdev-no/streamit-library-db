package no.iktdev.library.db.datasource

import org.jetbrains.exposed.sql.Database

open class DataSource(val connectionUrl: String, val databaseName: String, val address: String, val username: String, val password: String) {

    open fun createDatabase(): Database? {
        return toDatabase()
    }

     protected fun toDatabaseServerConnection(): Database {
        return Database.connect(
            "$connectionUrl://$address",
            user = username,
            password = password
        )
    }

    fun toDatabase(): Database {
        return Database.connect(
            "$connectionUrl://$address/$databaseName",
            user = username,
            password = password
        )
    }

}
