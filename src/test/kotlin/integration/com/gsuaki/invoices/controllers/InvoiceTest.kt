package integration.com.gsuaki.invoices.controllers

import com.fasterxml.jackson.core.type.TypeReference
import com.gsuaki.invoices.configs.Json
import com.gsuaki.invoices.controllers.input.InvoiceInput
import com.gsuaki.invoices.domain.Invoice
import integration.BaseConfig
import integration.MySqlExtension
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.random.Random

@ExtendWith(MySqlExtension::class)
@TestInstance(Lifecycle.PER_CLASS)
class InvoiceTest : BaseConfig() {

  @Test
  fun testCreateInvoice() = withFullContext {
    // given
    val payload = InvoiceInput(ownerId = 1239L)

    // when
    val call = handleRequest(HttpMethod.Post, "/invoices") {
      json()
      setBody(Json.writeValueAsString(payload))
    }

    // then
    with(call) {
      assertEquals(HttpStatusCode.Created, response.status())

      val invoice = Json.readValue(response.byteContent!!, Invoice::class.java)

      assertEquals(1239L, invoice.ownerId)
    }
  }

  @Test
  fun `test getOne when ID is not present should return unprocessable`() =
    withFullContext {
      // given
      val ownerId = Random(123).nextInt(from = 10, until = 1000)
      // when
      val call = handleRequest(HttpMethod.Get, "/invoices/?owner.id=$ownerId") { json() }

      // then
      with(call) {
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status())
      }
    }

  @Test
  fun `test getOne when ID is not found should return unprocessable`() =
    withFullContext {
      //given
      val id = Random(123).nextInt(from = 10, until = 1000)
      val ownerId = Random(123).nextInt(from = 10, until = 1000)
      // when
      val call = handleRequest(HttpMethod.Get, "/invoices/$id?owner.id=$ownerId") { json() }

      // then
      with(call) {
        assertEquals(HttpStatusCode.NotFound, response.status())
      }
    }

  @Test
  fun `test getOne when ID found should return OK`() =
    withFullContext {
      //given
      val invoice = createInvoice(this)

      // when
      val call = handleRequest(
        HttpMethod.Get,
        "/invoices/${invoice.id}?owner.id=${invoice.ownerId}"
      ) { json() }

      // then
      with(call) {
        assertEquals(HttpStatusCode.OK, response.status())
      }
    }

  @Test
  fun `test getAll when ID found should return OK`() =
    withFullContext {
      //given
      createInvoice(this)
      createInvoice(this)

      // when
      val call = handleRequest(HttpMethod.Get, "/invoices") { json() }

      // then
      with(call) {
        assertEquals(HttpStatusCode.OK, response.status())

        val invoices =
          Json.readValue(response.byteContent!!, object : TypeReference<List<Invoice>>() {})

        assertEquals(2, invoices.size)
      }
    }

  private fun createInvoice(app: TestApplicationEngine): Invoice {
    return app.handleRequest(HttpMethod.Post, "/invoices") {
      json()
      setBody(Json.writeValueAsString(InvoiceInput(ownerId = 1239L)))
    }
      .let { Json.readValue(it.response.byteContent!!, Invoice::class.java) }
  }
}
