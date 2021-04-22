package com.gsuaki.invoices.controllers

import com.gsuaki.invoices.controllers.input.InvoiceInput
import com.gsuaki.invoices.controllers.output.ApiError
import com.gsuaki.invoices.created
import com.gsuaki.invoices.services.InvoiceService
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.getOrFail
import javax.inject.Inject

class InvoicesController(
  @Inject private val service: InvoiceService
) {

  suspend fun getAll(call: ApplicationCall) {
    call.respond(service.getAll())
  }

  suspend fun getOne(call: ApplicationCall) {
    val id = call.parameters.getOrFail<Long>("id")
    val ownerId = call.parameters.getOrFail<Long>("owner.id")

    service.getOne(id, ownerId)
      .fold({ call.respond(HttpStatusCode.InternalServerError, ApiError(it)) }) { invoice ->
        invoice
          ?.let { call.respond(HttpStatusCode.OK, it) }
          ?: call.respond(HttpStatusCode.NotFound)
      }
  }

  suspend fun insert(call: ApplicationCall) {
    call.receive<InvoiceInput>()
      .let { service.insert(it) }
      .fold(
        { call.respond(HttpStatusCode.InternalServerError, it) },
        { call.created(it.toString(), it) }
      )
  }
}