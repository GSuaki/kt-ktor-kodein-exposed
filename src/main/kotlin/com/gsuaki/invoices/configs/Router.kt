package com.gsuaki.invoices

import com.gsuaki.invoices.controllers.InvoicesController
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
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
    get { controller.getAll(call) }

    get("/{id}") { controller.getOne(call) }

    post { controller.insert(call) }
  }
}

suspend fun ApplicationCall.created(resourceId: String, body: Any) {
  response.status(HttpStatusCode.Created)
  response.header("Location", "${request.uri}/$resourceId")

  respond(body)
}