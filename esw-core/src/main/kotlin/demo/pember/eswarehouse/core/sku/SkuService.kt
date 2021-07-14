package demo.pember.eswarehouse.core.sku

import demo.pember.eswarehouse.core.commands.RegisterSku
import io.cqrs.core.event.EventRepository

class SkuService(private val eventRepository: EventRepository) {

    fun registerSku(command: RegisterSku): SKU {
        val result = SKU(command.chosenSku)
            .loadCurrentState(eventRepository)
            .register(command)
        eventRepository.write(result.uncommittedEventEnvelopes)
        return result.capturedAggregate
    }
}