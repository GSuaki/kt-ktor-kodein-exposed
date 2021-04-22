package com.gsuaki.invoices.services.impl

import arrow.core.Either
import com.gsuaki.invoices.controllers.input.InvoiceInput
import com.gsuaki.invoices.domain.Invoice
import com.gsuaki.invoices.repositories.InvoiceRepository
import com.gsuaki.invoices.services.InvoiceService
import javax.inject.Inject
import org.jetbrains.exposed.sql.transactions.transaction

class DefaultInvoiceService(
  @Inject private val repository: InvoiceRepository
) : InvoiceService {

  override fun getAll(): List<Invoice> {
    return runCatching {
      transaction {
        repository.findAll().map { it.toDomain() }
      }
    }
      .getOrElse { emptyList() }
  }

  override fun getOne(id: Long, ownerId: Long): Either<Throwable, Invoice?> {
    return Either.catch {
      transaction {
        repository.findByIdAndOwnerId(id, ownerId)?.toDomain()
      }
    }
  }

  override fun insert(input: InvoiceInput): Either<Throwable, Invoice> {
    return Either.catch {
      transaction {
        repository.save(Invoice(ownerId = input.ownerId))
          .toDomain()
      }
    }
  }
}