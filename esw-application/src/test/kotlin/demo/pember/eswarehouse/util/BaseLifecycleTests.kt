package demo.pember.eswarehouse.util

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.PropertySource
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.Properties
import kotlin.collections.listOf


@Testcontainers
open class BaseLifecycleTests {

    companion object {
        lateinit var postgreSQLContainer: PostgreSQLContainer<*>
        lateinit var embeddedServer: EmbeddedServer
        lateinit var httpClient: HttpClient

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            postgreSQLContainer = PostgreSQLContainer<PostgreSQLContainer<*>>("postgres:13.1")
                .withDatabaseName("warehouse-test")
                .withUsername("ttestington")
                .withPassword("swordfish")
//            postgreSQLContainer.portBindings = listOf("55044:5432")
            postgreSQLContainer.start()
            assertTrue(postgreSQLContainer.isRunning)

            Flyway
                .configure()
                    .dataSource(postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password)
                    .load()
                .migrate()

            val props = Properties()
            props.setProperty("user", postgreSQLContainer.username)
            props.setProperty("password", postgreSQLContainer.password)
            println("Connecting to ${postgreSQLContainer.jdbcUrl}")
            val conn = DriverManager.getConnection(postgreSQLContainer.jdbcUrl, props);

            val st: Statement = conn.createStatement()
            val rs: ResultSet = st.executeQuery("SELECT 1 FROM events")
            while (rs.next()) {
                println("Column 1 returned ${rs.getString(1)}")
            }
            rs.close()
            st.close()

            val serverProps = mutableMapOf<String, Any>()
            serverProps["database.username"] = postgreSQLContainer.username
            serverProps["database.password"] = postgreSQLContainer.password
            serverProps["database.url"] = postgreSQLContainer.jdbcUrl

            embeddedServer = ApplicationContext.run(
                EmbeddedServer::class.java, PropertySource.of(
                    "database-override",
                   serverProps
                )
            )
            httpClient = embeddedServer.applicationContext.getBean(HttpClient::class.java)
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            postgreSQLContainer.stop()
        }


    }
}