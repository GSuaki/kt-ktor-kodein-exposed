package com.gsuaki.invoices.configs

import com.gsuaki.invoices.configs.Json.setUpMapper
import com.gsuaki.invoices.controllers.InvoicesController
import com.gsuaki.invoices.invoices
import com.gsuaki.invoices.ping
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import org.kodein.di.instance
import org.kodein.di.newInstance

fun Application.configure(profile: String = "dev") {
  contentNegotiation()
  statusPage()
  database(profile = profile)
  routing(profile = profile)
}

fun Application.contentNegotiation() {
  install(ContentNegotiation) {
    jackson {
      setUpMapper()
    }
  }
}

fun Application.database(profile: String = "dev") {
  installMySQL(profile = profile)
}

fun Application.routing(profile: String = "dev") {

  val invoicesController by injection.newInstance { InvoicesController(instance()) }

  install(Routing) {
    ping()
    invoices(invoicesController)
  }
}

fun Application.statusPage() {
  install(StatusPages) {
    errorHandler()
  }
}