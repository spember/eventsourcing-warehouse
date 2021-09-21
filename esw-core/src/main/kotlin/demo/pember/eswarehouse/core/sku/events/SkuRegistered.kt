package demo.pember.eswarehouse.core.sku.events

import io.cqrs.core.event.Event

/**
 * Signifies that a SKU has been registered or created within our Warehouse; we may now sell it (assuming we have)
 * inventory
 */
class SkuRegistered(val name: String): Event
// note that we don't store the SKUid on the event; it's the entity / aggregate id and is thus on the Envelope