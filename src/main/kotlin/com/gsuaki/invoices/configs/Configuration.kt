package com.gsuaki.invoices.configs

import com.gsuaki.invoices.configs.Json.setUpMapper
import com.gsuaki.invoices.controllers.InvoicesController
import com.gsuaki.invoices.invoices
import com.gsuaki.invoices.ping
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import org.kodein.di.newInstance

fun Application.configure(dev: Boolean = false) {
  contentNegotiation()
  routing(dev = dev)
}

private fun Application.contentNegotiation() {
  install(ContentNegotiation) {
    jackson {
      setUpMapper()
    }
  }
}

private fun Application.routing(dev: Boolean = false) {

  val invoicesController by injection.newInstance { InvoicesController() }

  install(Routing) {
    ping()
    invoices(invoicesController)
  }
}