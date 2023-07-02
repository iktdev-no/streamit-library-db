package no.iktdev.streamit.library.db.datasource

import no.iktdev.streamit.library.db.DatabaseEnv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class MySqlDataSource(databaseName: String, address: String, port: String, username: String, password: String): DataSource("jdbc:mysql", databaseName =  databaseName, address =  address, port = port, username = username, password = password) {
    companion object {
        fun fromDatabaseEnv(): MySqlDataSource {
            if (DatabaseEnv.database.isNullOrBlank()) throw RuntimeException("Database name is not defined in 'DATABASE_NAME'")
            if (DatabaseEnv.username.isNullOrBlank()) throw RuntimeException("Database username is not defined in 'DATABASE_USERNAME'")
            if (DatabaseEnv.address.isNullOrBlank()) throw RuntimeException("Database address is not defined in 'DATABASE_ADDRESS'")
            return MySqlDataSource(
                databaseName = DatabaseEnv.database,
                address = DatabaseEnv.address,
                port = DatabaseEnv.port ?: "",
                username = DatabaseEnv.username,
                password = DatabaseEnv.password ?: ""
            )
        }
    }

    override fun createDatabase(): Database? {
        val ok = transaction(toDatabaseServerConnection()) {
            val tmc = TransactionManager.current().connection
            val query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$databaseName'"
            val stmt = tmc.prepareStatement(query, true)

            val resultSet = stmt.executeQuery()
            val databaseExists = resultSet.next()

            if (!databaseExists) {
                try {
                    exec("CREATE DATABASE $databaseName")
                    println("Database $databaseName created.")
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            } else {
                println("Database $databaseName already exists.")
                true
            }
        }

        return if (ok) super.createDatabase() else null
    }

}