package demo.pember.eswarehouse.core.sku.events

import io.cqrs.core.event.Event

class PriceUpdated(val updatedPriceInCents: Long): Event