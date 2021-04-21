package com.gsuaki.invoices.controllers.output

data class ApiError(
  val message: String
) {

  constructor(ex: Throwable) : this(ex.message ?: "Internal server error")
}