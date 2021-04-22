package com.gsuaki.invoices.configs

import com.gsuaki.invoices.configs.Json.setUpMapper
import com.gsuaki.invoices.configs.Profile.DEV
import com.gsuaki.invoices.controllers.InvoicesController
import com.gsuaki.invoices.invoices
import com.gsuaki.invoices.ping
import io.ktor.application.Application
import io.ktor.application.ApplicationEnvironment
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import org.kodein.di.jxinject.jx
import org.kodein.di.ktor.closestDI

fun Application.contentNegotiation() {
  install(ContentNegotiation) {
    jackson {
      setUpMapper()
    }
  }
}

fun Application.database(profile: Profile) {
  installMySQL(profile = profile)
}

fun Application.routing(profile: Profile) {
  val invoicesController = closestDI().jx.newInstance<InvoicesController>()

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

fun ApplicationEnvironment.activeProfile(): Profile =
  runCatching { config.property("ktor.deployment.environment").getString() }
    .map { Profile.find(it) ?: DEV }
    .getOrDefault(DEV)