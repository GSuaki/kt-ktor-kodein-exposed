package integration

import com.gsuaki.invoices.start
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.withTestApplication as withKtorTestApplication

abstract class BaseConfig {

  fun <R> withTestApplication(test: TestApplicationEngine.() -> R) {
    withKtorTestApplication({ start(dev = true) }, test)
  }

  protected fun TestApplicationRequest.json() {
    addHeader(HttpHeaders.Accept, Application.Json.toString())
    addHeader(HttpHeaders.ContentType, Application.Json.toString())
  }
}