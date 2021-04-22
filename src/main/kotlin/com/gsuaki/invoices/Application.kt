package com.gsuaki.invoices

import com.gsuaki.invoices.configs.activeProfile
import com.gsuaki.invoices.configs.contentNegotiation
import com.gsuaki.invoices.configs.database
import com.gsuaki.invoices.configs.injection
import com.gsuaki.invoices.configs.routing
import com.gsuaki.invoices.configs.statusPage
import io.ktor.application.Application
import io.ktor.server.cio.EngineMain
import org.kodein.di.ktor.di

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.start() {
  val profile = environment.activeProfile()

  di {
    injection(profile = profile)
  }

  contentNegotiation()
  database(profile = profile)
  routing(profile = profile)
  statusPage()
}