package demo.pember.eswarehouse.core.warehouse

import demo.pember.eswarehouse.core.identifiers.WarehouseId
import io.cqrs.core.Aggregate
import io.cqrs.core.event.Event
import io.cqrs.core.event.EventEnvelope
import io.cqrs.core.identifiers.EntityId

class Warehouse(id: WarehouseId): Aggregate<WarehouseId, Warehouse>(id) {

    var name: String = ""
        private set

    var yearOpened: Int = -1
        private set

    // how much room do we have for stuff? for simplicity, using a basic inventory count, but sq meters might be better
    var totalUnitCapacity: Long = 0
        private set

    // the current inventory count. Compare with total capacity to see if we have room for things
    var currentInventory: Long = 0
        private set


    var address: String = ""
        private set

    // note that we're not keeping track of actual skus and their inventory here. that's better suited for a
    // query model, perhaps


    override fun handleEventApply(p0: EventEnvelope<out Event, out EntityId<*>>) {
        TODO("Not yet implemented")
    }
}