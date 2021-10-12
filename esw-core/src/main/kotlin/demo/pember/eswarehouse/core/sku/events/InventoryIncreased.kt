package demo.pember.eswarehouse.core.sku.events

import io.cqrs.core.event.Event

class InventoryIncreased(val amount: Int = 1): Event