package com.gsuaki.invoices.configs

import com.gsuaki.invoices.controllers.InvoicesController
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

private val controllers = DI.Module(name = "controllers") {
  bind<InvoicesController>() with singleton { InvoicesController() }
}

val injection = DI {
  import(controllers)
}