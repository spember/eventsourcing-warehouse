package demo.pember.eswarehouse.app

import com.fasterxml.jackson.databind.ObjectMapper
import demo.pember.eswarehouse.app.config.DatabaseConfiguration
import demo.pember.eswarehouse.core.sku.SkuService
import io.cqrs.core.event.EventRepository
import io.cqrs.core.event.PostgresEventRepository
import io.micronaut.context.annotation.Factory
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.Driver
import org.slf4j.LoggerFactory
import java.sql.Connection
import javax.inject.Singleton
import java.util.Properties

/**
 * Responsible for Wiring up Beans for the Core module.
 * This will likely need to come last in DI order as the
 */
@Factory
class CoreBeanFactory {
    companion object {
        val log = LoggerFactory.getLogger(CoreBeanFactory::class.java)!!
    }

    @Singleton
    fun establishSqlConnection(databaseConfiguration: DatabaseConfiguration): Connection {
        val loginProps = Properties()
        loginProps.setProperty("user", databaseConfiguration.username)
        loginProps.setProperty("password", databaseConfiguration.password)
        log.info("Attempting Micronaut connection to ${databaseConfiguration.url}")
        return  Driver().connect(
            databaseConfiguration.url,
            loginProps
        )!!
    }

    @Singleton
    fun createJooq(postgresConnection: Connection): DSLContext {
        return DSL.using(
            postgresConnection,
            SQLDialect.POSTGRES
        )
    }

    @Singleton
    fun createObjectMapper(): ObjectMapper {
        return ObjectMapper()
    }

    @Singleton
    fun createEventRepository(jooq: DSLContext, objectMapper: ObjectMapper): EventRepository =
        PostgresEventRepository(jooq, objectMapper)


    // the following are the actual core beans. Consider moving the above to a different module

    @Singleton
    fun createSkuService(eventRepository: EventRepository): SkuService = SkuService(eventRepository)
}