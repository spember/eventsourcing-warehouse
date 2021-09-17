package demo.pember.eswarehouse.app

import demo.pember.eswarehouse.app.config.DatabaseConfiguration
import demo.pember.eswarehouse.core.sku.SkuService
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import javax.inject.Singleton

@Singleton
class OnStartup(val databaseConfig: DatabaseConfiguration, val skuService: SkuService): ApplicationEventListener<ServerStartupEvent> {
    override fun onApplicationEvent(event: ServerStartupEvent?) {
    }
}