package demo.pember.eswarehouse.app

import EmployeeId
import demo.pember.eswarehouse.app.config.DatabaseConfiguration
import demo.pember.eswarehouse.core.commands.RegisterSku
import demo.pember.eswarehouse.core.identifiers.SkuCode
import demo.pember.eswarehouse.core.sku.SkuService
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import javax.inject.Singleton

@Singleton
class OnStartup(val databaseConfig: DatabaseConfiguration, val skuService: SkuService): ApplicationEventListener<ServerStartupEvent> {
    override fun onApplicationEvent(event: ServerStartupEvent?) {
        println("we have config ${databaseConfig.username}, ${databaseConfig.password}, ${databaseConfig.url}")
        skuService.registerSku(RegisterSku(
            EmployeeId("onetwothree"),
            SkuCode("My sku"),
            "Test product"
        ))
    }
}