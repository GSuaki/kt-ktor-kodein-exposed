package integration.com.gsuaki.invoices.controllers

import com.fasterxml.jackson.core.type.TypeReference
import com.gsuaki.invoices.configs.Json
import com.gsuaki.invoices.controllers.input.InvoiceInput
import com.gsuaki.invoices.domain.Invoice
import integration.BaseConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class InvoiceTest : BaseConfig() {

  @Test
  fun testCreateInvoice() = withTestApplication {
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
    withTestApplication {
      // when
      val call = handleRequest(HttpMethod.Get, "/invoices/") { json() }

      // then
      with(call) {
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status())
      }
    }

  @Test
  fun `test getOne when ID is not found should return unprocessable`() =
    withTestApplication {
      //given
      val id = Random(123).nextInt(from = 10, until = 1000)
      // when
      val call = handleRequest(HttpMethod.Get, "/invoices/$id") { json() }

      // then
      with(call) {
        assertEquals(HttpStatusCode.NotFound, response.status())
      }
    }

  @Test
  fun `test getOne when ID found should return OK`() =
    withTestApplication {
      //given
      val id = createInvoice(this)

      // when
      val call = handleRequest(HttpMethod.Get, "/invoices/$id") { json() }

      // then
      with(call) {
        assertEquals(HttpStatusCode.OK, response.status())
      }
    }

  @Test
  fun `test getAll when ID found should return OK`() =
    withTestApplication {
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

  private fun createInvoice(app: TestApplicationEngine): Long {
    return app.handleRequest(HttpMethod.Post, "/invoices") {
      json()
      setBody(Json.writeValueAsString(InvoiceInput(ownerId = 1239L)))
    }
      .let { Json.readValue(it.response.byteContent!!, Invoice::class.java).id }
  }
}
