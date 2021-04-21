package com.gsuaki.invoices.services

import arrow.core.Either
import com.gsuaki.invoices.controllers.input.InvoiceInput
import com.gsuaki.invoices.domain.Invoice

interface InvoiceService {

  fun getAll(): List<Invoice>

  fun getOne(id: Long, ownerId: Long): Either<Throwable, Invoice?>

  fun insert(input: InvoiceInput): Either<Throwable, Invoice>
}