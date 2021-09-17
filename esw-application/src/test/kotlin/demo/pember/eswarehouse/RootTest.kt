package demo.pember.eswarehouse

import demo.pember.eswarehouse.util.BaseLifecycleTests
import io.micronaut.http.HttpRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RootTest: BaseLifecycleTests() {


    @Test
    fun testItWorks() {
        assertTrue(embeddedServer.isRunning)
        assertTrue(embeddedServer.applicationContext.environment.activeNames.contains("test"));
    }

    @Test
    fun testHelloWorldResponse() {
        val response: String = httpClient.toBlocking()
            .retrieve(HttpRequest.GET<String>("${embeddedServer.uri}/"))
        assertEquals("Testing", response)
    }
}