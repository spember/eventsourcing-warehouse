package demo.pember.eswarehouse.core.sku

import demo.pember.eswarehouse.core.commands.RegisterSku
import demo.pember.eswarehouse.core.identifiers.SkuCode
import io.cqrs.core.event.EventRepository

class SkuService(private val eventRepository: EventRepository) {

    fun registerSku(command: RegisterSku): SKU {
        val result = SKU(command.chosenSku)
            .loadCurrentState(eventRepository)
            .register(command)
        eventRepository.write(result.uncommittedEventEnvelopes)
        return result.capturedAggregate
    }

    fun fetch(skuCode: SkuCode): SKU? {
        return SKU(skuCode)
            .loadCurrentState(eventRepository)
    }
}