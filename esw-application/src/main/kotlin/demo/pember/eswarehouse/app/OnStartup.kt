package demo.pember.eswarehouse.app

import io.cqrs.core.event.EventRegistry
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import javax.inject.Singleton

@Singleton
class OnStartup(private val eventRegistry: EventRegistry): ApplicationEventListener<ServerStartupEvent> {
    override fun onApplicationEvent(event: ServerStartupEvent?) {
        eventRegistry.scan("demo.pember")
    }
}