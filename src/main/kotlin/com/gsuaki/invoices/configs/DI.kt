package com.gsuaki.invoices.configs

import com.gsuaki.invoices.controllers.InvoicesController
import com.gsuaki.invoices.services.InvoiceService
import com.gsuaki.invoices.services.impl.DefaultInvoiceService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

private val services = DI.Module(name = "services") {
  bind<InvoiceService>() with singleton { DefaultInvoiceService() }
}

private val controllers = DI.Module(name = "controllers") {
  bind<InvoicesController>() with singleton { InvoicesController(instance()) }
}

val injection = DI {
  import(controllers)
  import(services)
}