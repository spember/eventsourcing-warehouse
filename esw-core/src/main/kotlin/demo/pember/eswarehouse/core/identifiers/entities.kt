package demo.pember.eswarehouse.core.identifiers

import io.cqrs.core.identifiers.EntityId

/**
 * Primary identifier for SKUs, a product class.
 *
 * In a larger system we might have elaborate validation for this id, but for now just a string
 */
class SkuCode(value: String): EntityId<String>(value)

/**
 * PI for Warehouse, the physical store of our skus
 */
class WarehouseId(value: String): EntityId<String>(value)