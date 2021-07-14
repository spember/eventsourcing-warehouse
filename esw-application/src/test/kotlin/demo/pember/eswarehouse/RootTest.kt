package demo.pember.eswarehouse

import demo.pember.eswarehouse.util.BaseLifecycleTests
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.inject.Inject


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