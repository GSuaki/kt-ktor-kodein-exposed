package integration

import com.gsuaki.invoices.configs.contentNegotiation
import com.gsuaki.invoices.configs.installMySQLLocal
import com.gsuaki.invoices.configs.routing
import com.gsuaki.invoices.configs.statusPage
import com.gsuaki.invoices.repositories.InvoiceRepository
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.extension.ExtendWith
import io.ktor.server.testing.withTestApplication as withKtorTestApplication

@ExtendWith(MySqlExtension::class)
abstract class BaseConfig {

  fun <R> withTestApplication(test: TestApplicationEngine.() -> R) {
    withKtorTestApplication({
      contentNegotiation()
      statusPage()
      installMySQLLocal(MySqlExtension.CONTAINER.get().jdbcUrl).also {
        transaction { SchemaUtils.create(InvoiceRepository) }
      }
      routing(profile = "test")
    }, test)
  }

  protected fun TestApplicationRequest.json() {
    addHeader(HttpHeaders.Accept, Application.Json.toString())
    addHeader(HttpHeaders.ContentType, Application.Json.toString())
  }
}