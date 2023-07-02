package no.iktdev.streamit.library.db

object DatabaseEnv {
    val address: String? = System.getenv("DATABASE_ADDRESS")
    val port: String? = System.getenv("DATABASE_PORT")
    val username: String? = System.getenv("DATABASE_USERNAME")
    val password: String? = System.getenv("DATABASE_PASSWORD")
    val database: String? = System.getenv("DATABASE_NAME")
}