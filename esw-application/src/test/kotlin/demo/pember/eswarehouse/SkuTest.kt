package demo.pember.eswarehouse

import demo.pember.eswarehouse.controllers.RegisterSkuParameters
import demo.pember.eswarehouse.controllers.SkuDetails
import demo.pember.eswarehouse.util.BaseLifecycleTests
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SkuTest: BaseLifecycleTests() {

    @Test
    fun `a missing sku should return a 404`() {
        assertThrows<HttpClientResponseException> {
            httpClient.toBlocking()
                .exchange(HttpRequest.GET<SkuDetails>("${embeddedServer.uri}/sku/missing"), SkuDetails::class.java)
        }
    }

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
        assertEquals(response.priceInCents, 0L)
    }

    @Test
    fun `a registered sku should be retievable`() {
        val code = "sk-123"

        assertThrows<HttpClientResponseException> {
            httpClient.toBlocking()
                .exchange(HttpRequest.GET<SkuDetails>("${embeddedServer.uri}/sku/sk-123"), SkuDetails::class.java)
        }

        val response = httpClient.toBlocking()
            .exchange(
                HttpRequest.POST("${embeddedServer.uri}/sku", RegisterSkuParameters(code, "Singing Knight"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON),
                SkuDetails::class.java
            ).body()

        assertNotNull(response)

        val finalSkuDetails = httpClient.toBlocking()
            .exchange(HttpRequest.GET<SkuDetails>("${embeddedServer.uri}/sku/sk-123"), SkuDetails::class.java)
            .body()

        assertEquals(finalSkuDetails!!.code, code)
        assertEquals(finalSkuDetails.name, "Singing Knight")
    }
}