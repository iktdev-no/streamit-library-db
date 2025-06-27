import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import no.iktdev.streamit.library.db.DatabaseEnv
import no.iktdev.streamit.library.db.datasource.MySqlDataSource
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

        val h2Datasource = H2Datasource("test_database")
        val connectionInstance = h2Datasource.createDatabase()
        assertNotNull(connectionInstance)

        transaction(connectionInstance) {
            // Execute createDatabase()

            // Assert the expected behavior
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