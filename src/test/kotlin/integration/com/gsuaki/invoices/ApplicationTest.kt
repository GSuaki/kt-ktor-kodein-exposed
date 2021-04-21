package integration.com.gsuaki.invoices

import com.gsuaki.invoices.start
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

  @Test
  fun testPing() {
    withTestApplication({ start(dev = true) }) {

      handleRequest(HttpMethod.Get, "/ping").apply {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals("HELLO WORLD!", response.content)
      }
    }
  }
}
