import no.iktdev.streamit.library.db.datasource.MySqlDataSource
import org.jetbrains.exposed.sql.Database

class H2Datasource(databaseName: String) : MySqlDataSource(
    databaseName = databaseName,
    address = "",
    port = "",
    username = "",
    password = ""
) {

    override fun createDatabaseStatement(): String {
        return "CREATE SCHEMA $databaseName"
    }

    override fun toConnectionUrl(): String {
        return "jdbc:h2:mem:$databaseName;MODE=MYSQL;DB_CLOSE_DELAY=-1;" // "jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1;"
    }

    fun toDatabase2(): Database {
        return Database.connect(
            toConnectionUrl(),
            user = username,
            password = password
        )
    }

}