package com.gsuaki.invoices

import com.gsuaki.invoices.controllers.InvoicesController
import com.gsuaki.invoices.controllers.input.InvoiceInput
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.uri
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.ping() {
  get("/ping") {
    call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
  }
}

fun Route.invoices(controller: InvoicesController) {
  route("/invoices") {
    get {
      call.respond(controller.getAll())
    }

    get("/{id}") {
      runCatching { call.parameters["id"]?.toLong() }.getOrNull()
        ?.let { controller.getOne(it) }
        ?.fold({ call.respond(HttpStatusCode.InternalServerError, it) }) { invoice ->
          invoice
            ?.let { call.respond(HttpStatusCode.OK, it) }
            ?: call.respond(HttpStatusCode.NotFound)
        }
        ?: call.respond(HttpStatusCode.UnprocessableEntity)
    }

    post {
      call.receive<InvoiceInput>()
        .let { controller.insert(it) }
        .fold(
          { call.respond(HttpStatusCode.InternalServerError, it) },
          { call.created(it.toString(), it) }
        )
    }
  }
}

private suspend fun ApplicationCall.created(resourceId: String, body: Any) {
  response.status(HttpStatusCode.Created)
  response.header("Location", "${request.uri}/$resourceId")

  respond(body)
}