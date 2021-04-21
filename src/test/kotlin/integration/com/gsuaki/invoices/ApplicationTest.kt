package integration.com.gsuaki.invoices

import com.gsuaki.invoices.start
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApplicationTest {

  @Test
  fun testPing() {
    withTestApplication({ start() }) {

      handleRequest(HttpMethod.Get, "/ping").apply {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals("HELLO WORLD!", response.content)
      }
    }
  }
}
