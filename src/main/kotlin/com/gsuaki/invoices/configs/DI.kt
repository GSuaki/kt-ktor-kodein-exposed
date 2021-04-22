package com.gsuaki.invoices.configs

import com.gsuaki.invoices.controllers.InvoicesController
import com.gsuaki.invoices.repositories.InvoiceRepository
import com.gsuaki.invoices.repositories.impl.DefaultInvoiceRepository
import com.gsuaki.invoices.services.InvoiceService
import com.gsuaki.invoices.services.impl.DefaultInvoiceService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.jxinject.jxInjectorModule
import org.kodein.di.singleton

private val repositories = DI.Module(name = "repositories") {
  bind<InvoiceRepository>() with singleton { DefaultInvoiceRepository() }
}

private val services = DI.Module(name = "services") {
  bind<InvoiceService>() with singleton { DefaultInvoiceService(instance()) }
}

private val controllers = DI.Module(name = "controllers") {
  bind<InvoicesController>() with singleton { InvoicesController(instance()) }
}

fun DI.MainBuilder.injection(profile: Profile) {
  import(jxInjectorModule)
  import(controllers, allowOverride = true)
  import(repositories, allowOverride = true)
  import(services, allowOverride = true)
}