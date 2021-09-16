package demo.pember.eswarehouse

import demo.pember.eswarehouse.controllers.RegisterSkuParameters
import demo.pember.eswarehouse.controllers.SkuDetails
import demo.pember.eswarehouse.core.identifiers.SkuCode
import demo.pember.eswarehouse.core.sku.SKU
import demo.pember.eswarehouse.util.BaseLifecycleTests
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SkuTest: BaseLifecycleTests() {

    /**
     *
     */
    @Test
    fun `a registered sku should be retrievable`() {
        val response = httpClient.toBlocking()
            .exchange(
                HttpRequest.POST("${embeddedServer.uri}/sku", RegisterSkuParameters("BRK-01", "Custom Broker"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON),
                SkuDetails::class.java
            ).body()
        assertNotNull(response)
        assertEquals(response!!.name, "Custom Broker")
        assertEquals(response.code, "BRK-01")
    }
}