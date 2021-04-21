package com.gsuaki.invoices.services.impl

import arrow.core.Either
import com.gsuaki.invoices.controllers.input.InvoiceInput
import com.gsuaki.invoices.domain.Invoice
import com.gsuaki.invoices.domain.InvoiceStatus.PENDING
import com.gsuaki.invoices.repositories.InvoiceEntity
import com.gsuaki.invoices.services.InvoiceService
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class DefaultInvoiceService : InvoiceService {

  override fun getAll(): List<Invoice> {
    return runCatching {
      transaction {
        InvoiceEntity.all().map { it.toDomain() }
      }
    }
      .getOrElse { emptyList() }
  }

  override fun getOne(id: Long, ownerId: Long): Either<Throwable, Invoice?> {
    return Either.catch {
      transaction {
        InvoiceEntity.findByIdAndOwnerId(id, ownerId)?.toDomain()
      }
    }
  }

  override fun insert(input: InvoiceInput): Either<Throwable, Invoice> {
    return Either.catch {
      transaction {
        InvoiceEntity.new {
          ownerId = input.ownerId
          status = PENDING
          createdAt = LocalDateTime.now()
        }
          .toDomain()
      }
    }
  }
}