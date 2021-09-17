package demo.pember.eswarehouse.core.sku

import demo.pember.eswarehouse.core.commands.RegisterSku
import demo.pember.eswarehouse.core.commands.UpdateMsrp
import demo.pember.eswarehouse.core.identifiers.SkuCode
import io.cqrs.core.Command
import io.cqrs.core.event.EventRepository
import io.cqrs.core.identifiers.UserId
import io.cqrs.core.process.AggregateMutationResult

class SkuService(private val eventRepository: EventRepository) {
    /*
        I will note that I'm not super happy with this design, and it's exemplified in this class.
        This service essentially acts as a buffer between the Controller operations and the world of the underlying
        Aggregates / event repositories.
        It may be a sign that we could push the eventRepository into the Aggregate itself
     */

    /*
        Query Operations
     */
    fun fetch(skuCode: SkuCode): SKU? {
        return SKU(skuCode)
            .loadCurrentState(eventRepository)
            .takeIf { sku -> !sku.isBare && sku.active }
    }

    /*
        Mutation Operations
     */

    fun registerSku(command: RegisterSku): SKU {
        return handleResultForLoadedSku(command.chosenSku) { loadedSku ->  loadedSku.register(command)}
            .capturedAggregate
    }

    fun updateMsrp(command: UpdateMsrp): SKU {
        return handleResultForLoadedSku(command.code) {loadedSku ->  loadedSku.updateMsrp(command) }
            .capturedAggregate
    }

    /**
     * An internal wrapper for loaded the SKU to current state, deferring to a supplied aggregate mutation function,
     * then persisting the resulting eventEnvelopes, if any
     */
    private fun <E: UserId<*>, C: Command<E>> handleResultForLoadedSku(skuCode: SkuCode, fn: (loadedSku: SKU) -> AggregateMutationResult<C, SKU>): AggregateMutationResult<C, SKU> {
        val result = fn(SKU(skuCode).loadCurrentState(eventRepository))
        eventRepository.write(result.uncommittedEventEnvelopes)
        return result
    }
}