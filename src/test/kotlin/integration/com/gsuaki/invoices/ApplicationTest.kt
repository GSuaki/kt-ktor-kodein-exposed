package integration.com.gsuaki.invoices

import integration.BaseConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

@TestInstance(Lifecycle.PER_CLASS)
class ApplicationTest : BaseConfig() {

  @Test
  fun testPing() = withRouterContext {
    handleRequest(HttpMethod.Get, "/ping").apply {
      assertEquals(HttpStatusCode.OK, response.status())
      assertEquals("HELLO WORLD!", response.content)
    }
  }
}
