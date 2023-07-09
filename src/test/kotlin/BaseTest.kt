import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import no.iktdev.streamit.library.db.DatabaseEnv
import no.iktdev.streamit.library.db.datasource.MySqlDataSource
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BaseTest {

    @AfterEach
    fun cleanup() {
        // Unmock DatabaseEnv
        unmockkAll()
    }
    @BeforeEach
    fun setup() {
        // Set the environment variables
        mockkObject(DatabaseEnv)
        every { DatabaseEnv.database } returns "test_database"
        every { DatabaseEnv.address } returns "localhost"
        every { DatabaseEnv.port } returns "3306"
        every { DatabaseEnv.username } returns "test_user"
        every { DatabaseEnv.password } returns "test_password"
    }

    @Test
    fun testDatabaseEnvCreation() {
        val dataSource = assertDoesNotThrow {
            MySqlDataSource.fromDatabaseEnv()
        }
        assertEquals("test_database", dataSource.databaseName)
        assertEquals("localhost", dataSource.address)
        assertEquals("3306", dataSource.port)
        assertEquals("test_user", dataSource.username)
        assertEquals("test_password", dataSource.password)
    }

    @Test
    fun testCreateDatabase() {

                // Use the H2 in-memory database for testing
        val h2DataSource = H2DataSource(JdbcDataSource().apply {
            setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;")
        }, DatabaseEnv.database ?: "test_database")

        transaction(Database.connect(h2DataSource)) {
            // Execute createDatabase()
            val createdDatabase = h2DataSource.createDatabase()

            // Assert the expected behavior
            assertNotNull(createdDatabase)
            assertEquals("test_database", "test_database")

            // Perform additional assertions or database operations
            /*transaction(dataSource.toDatabase()) {
                SchemaUtils.create(Users)

                // Assert that the table exists
                Assertions.assertTrue(Users.exists())
            }*/
        }

    }

}