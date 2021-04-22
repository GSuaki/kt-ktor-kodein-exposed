package integration

import com.gsuaki.invoices.configs.activeProfile
import com.gsuaki.invoices.configs.contentNegotiation
import com.gsuaki.invoices.configs.injection
import com.gsuaki.invoices.configs.installMySQLLocal
import com.gsuaki.invoices.configs.routing
import com.gsuaki.invoices.configs.statusPage
import com.gsuaki.invoices.repositories.tables.InvoiceTable
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.withTestApplication
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.ktor.di

abstract class BaseConfig {

  fun <R> withRouterContext(test: TestApplicationEngine.() -> R) {
    withTestApplication({
      val profile = environment.activeProfile()

      di { injection(profile = profile) }
      contentNegotiation()
      routing(profile = profile)
      statusPage()
    }, test)
  }

  fun <R> withFullContext(test: TestApplicationEngine.() -> R) {
    withTestApplication({
      val profile = environment.activeProfile()

      di { injection(profile = profile) }
      contentNegotiation()
      installMySQLLocal(MySqlExtension.CONTAINER.get().jdbcUrl).also {
        transaction { SchemaUtils.create(InvoiceTable) }
      }
      routing(profile = profile)
      statusPage()
    }, test)
  }

  protected fun TestApplicationRequest.json() {
    addHeader(HttpHeaders.Accept, Application.Json.toString())
    addHeader(HttpHeaders.ContentType, Application.Json.toString())
  }
}