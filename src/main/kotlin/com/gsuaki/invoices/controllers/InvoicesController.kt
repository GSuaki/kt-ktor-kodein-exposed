package com.gsuaki.invoices.controllers

import arrow.core.Either
import com.gsuaki.invoices.controllers.input.InvoiceInput
import com.gsuaki.invoices.domain.Invoice
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

class InvoicesController {

  fun getAll(): List<Invoice> {
    return runCatching { INVOICES }
      .getOrElse { emptyList() }
  }

  fun getOne(id: Long): Either<Throwable, Invoice?> {
    return Either.catch {
      INVOICES.find { it.id == id }
    }
  }

  fun insert(input: InvoiceInput): Either<Throwable, Invoice> {
    return Either.catch {
      Invoice(id = COUNTER.incrementAndGet(), ownerId = input.ownerId)
        .also { INVOICES.add(it) }
    }
  }

  companion object {
    val COUNTER = AtomicLong(0)
    val INVOICES = CopyOnWriteArrayList<Invoice>()
  }
}