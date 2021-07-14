package demo.pember.eswarehouse.app.config

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("database")
class DatabaseConfiguration {
    // jdbc:postgresql://localhost:55044/warehouse-test?loggerLevel=OFF
    lateinit var username: String
    lateinit var password: String
    var url: String = "jdbc:postgresql://localhost:5432/warehouse"
}