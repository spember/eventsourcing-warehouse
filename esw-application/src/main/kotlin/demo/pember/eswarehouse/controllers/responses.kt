package demo.pember.eswarehouse.controllers

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import demo.pember.eswarehouse.core.sku.SKU

/**
 * While at first glance these seem like arbitrary mapping code, these responses serve three purposes:
 *  - keep additional dependencies out of the -core module (e.g. jackson annotations). This is more of a purity and
 *  stylistic preference
 *  - provide a standard 'api response' that external systems adhere to and guard against changes in our underlying code
 *  from breaking contracts with other services
 *  - a mirror of the previous point, this allows us to decouple the internal workings of our service from the external
 *  view, and allows us to shape and evolve both the external responses and our internal code independently.
 *
 *  In other services one might see generated code by tools like Swagger in place of these objects
 */

/**
 *
 */
class SkuDetails(
    @JsonProperty("code") val code: String,
    @JsonProperty("revision") val revision: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("priceInCents") val priceInCents: Long,
    ) {

    companion object {
        fun from (entity: SKU) : SkuDetails {
            return SkuDetails(
                entity.id.value,
                entity.revision,
                entity.name,
                if (entity.priceInCents <= entity.msrpInCents) entity.priceInCents else entity.msrpInCents
            )
        }
    }
}

/**
 * Container for Event Data intended for Public consumption.
 * For test purposes this is effectively a minimized merging of the event envelope and the actual fields, but this
 * could be a location for stripping out sensitive fields
 */
class PublicEvent(
    @JsonProperty("entityId") val entityId: String,
    @JsonProperty("revision") val revision: Int,
    @JsonProperty("user") val userId: String,
    @JsonProperty("timeOccurred") val timeOccurred: String,
    @JsonProperty("event") val eventName: String,
)