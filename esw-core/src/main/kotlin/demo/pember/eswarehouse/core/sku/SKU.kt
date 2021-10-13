package demo.pember.eswarehouse.core.sku

import demo.pember.eswarehouse.core.commands.RegisterSku
import demo.pember.eswarehouse.core.commands.UpdateMsrp
import demo.pember.eswarehouse.core.identifiers.SkuCode
import demo.pember.eswarehouse.core.sku.events.InventoryIncreased
import demo.pember.eswarehouse.core.sku.events.MsrpUpdated
import demo.pember.eswarehouse.core.sku.events.PriceUpdated
import demo.pember.eswarehouse.core.sku.events.SkuRegistered
import io.cqrs.core.Aggregate
import io.cqrs.core.event.Event
import io.cqrs.core.event.EventEnvelope
import io.cqrs.core.event.EventRepository
import io.cqrs.core.identifiers.EntityId
import io.cqrs.core.process.AggregateMutationResult
import java.lang.RuntimeException

/**
 * A StockKeepingUnit reflects a particular Product we store in our warehouse. A SKU has a number of 'facets'
 * and category. Facets are generally a key / value (e.g. size: 'm', material: ["leater","metal"], color: "red") and
 * categories are generally a hierarchy or tag (e.g. "apparel -> shirts -> t-shirt")
 *
 * In this simple app we'll use very simplistic structures for facets and tags, but they could grow more complex
 * in a real system. In addition, this class will likely be overly simplistic and not have data like UPC.
 *
 * https://www.shopify.com/encyclopedia/stock-keeping-unit-sku
 *
 * Our SKU lifecycle will simulate setting up a new sku, marking it 'available to sell', and possibly discontinuing it.
 * This is to simulate an interface where a User sets up a new SKU, not with a single massive form, but with a
 * more "task-based" approach:
 *
 *  * declare a new SKU with a skuCode and name
 *  * 'assemble' the sku with pricing, facets, categories, obtain inventory, etc
 *  * However, a sku cannot be sold / shipped until it's activated. Activation requires a check on the sku to ensure
 *  that it's in a valid state
 *  * shipping / selling may only occur on 'active' skus
 *  * SKUs can be decommissioned: we no longer sell them, but our systems must be aware of them for accounting reasons,
 *  returns, etc.
 *
 *
 */
class SKU(code: SkuCode): Aggregate<SkuCode, SKU>(code) {

    var name: String = ""
        private set

    // how much we _should_ sell it for
    var msrpInCents: Long = 0
        private set

    // how much we _are_ selling it for
    var priceInCents: Long = 0
        private set

    // todo: change state of the class to reflect the state, rather than a boolean
    var active: Boolean = true
        private set

    var facets: List<Pair<Facet, String>> = mutableListOf()
        private set

    var categories: List<Categories> = mutableListOf()
        private set

    var currentInventory: Int = 0
        private set

    override fun loadCurrentState(eventRepository: EventRepository): SKU {
        super.loadCurrentState(eventRepository)
        return this
    }

    fun register(command: RegisterSku): AggregateMutationResult<RegisterSku, SKU> {
        val result = AggregateMutationResult(this, command)
        if (!this.isBare) {
            result.error(RuntimeException("A Sku already exists with code ${command.chosenSku}"))
        } else {
            result.addEvents(this,
                SkuRegistered(command.name),
                // these 'initializing' events aren't strictly necessary, but here for illustration
                MsrpUpdated(0),
                PriceUpdated(0)
            )
        }
        return result
    }

    fun updateMsrp(command: UpdateMsrp): AggregateMutationResult<UpdateMsrp, SKU> {
        val result = AggregateMutationResult(this, command)
        if (this.isBare || !this.active) {
            result.error(RuntimeException("Cannot update price on an invalid sku!"))
        } else if (command.updatedPrice <= 0) {
            result.error(IllegalStateException("Price cannot be 0 or less"))
        } else {
            result.addEvents(this, MsrpUpdated(command.updatedPrice))
        }
        return result
    }

    /**
     * The lifecycle of a SKU is generally fairly simple and involves creation, retirement, price adjustments, and
     * inventory changes.
     *
     *
     */
    override fun handleEventApply(envelope: EventEnvelope<out Event, out EntityId<*>>) {
        when(envelope.event::class) {
            // if I need access to timestamps, user ids, or the core entity id, I can also pass along the EventEnvelope
            SkuRegistered::class -> handle(envelope.event as SkuRegistered)
            MsrpUpdated::class -> handle(envelope.event as MsrpUpdated)
            PriceUpdated::class -> handle(envelope.event as PriceUpdated)
            InventoryIncreased::class -> println(envelope)
        }
    }

    private fun handle(event: SkuRegistered) {
        this.name = event.name
    }

    private fun handle(event: MsrpUpdated) {
        this.msrpInCents = event.updatedMsrp
        this.priceInCents = event.updatedMsrp
        // keeping price in lockstep with msrp until it is adjusted via
        // an 'discountApplied' event
    }

    private fun handle(event: PriceUpdated) {
        this.priceInCents = event.updatedPriceInCents
    }
}