package com.gsuaki.invoices.configs

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.gsuaki.invoices.configs.Json.setUpMapper
import com.gsuaki.invoices.controllers.InvoicesController
import com.gsuaki.invoices.ping
import com.gsuaki.invoices.invoices
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import java.text.SimpleDateFormat

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
  install(Routing) {
    ping()
    invoices(InvoicesController())
  }
}