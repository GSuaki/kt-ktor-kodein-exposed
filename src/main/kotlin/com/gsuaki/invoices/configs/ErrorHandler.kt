package com.gsuaki.invoices.configs

import com.gsuaki.invoices.controllers.output.ApiError
import io.ktor.application.call
import io.ktor.features.MissingRequestParameterException
import io.ktor.features.ParameterConversionException
import io.ktor.features.StatusPages.Configuration
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun Configuration.errorHandler() {

  exception<ParameterConversionException> { cause ->
    call.respond(HttpStatusCode.UnprocessableEntity, ApiError(cause.message!!))
  }

  exception<MissingRequestParameterException> { cause ->
    call.respond(HttpStatusCode.UnprocessableEntity, ApiError(cause.message!!))
  }

  exception<Throwable> { cause ->
    cause.printStackTrace()
    call.respond(HttpStatusCode.InternalServerError, ApiError(cause))
  }
}
