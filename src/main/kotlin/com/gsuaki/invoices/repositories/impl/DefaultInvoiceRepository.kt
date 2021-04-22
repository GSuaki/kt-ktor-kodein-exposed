package com.gsuaki.invoices.repositories.impl

import com.gsuaki.invoices.domain.Invoice
import com.gsuaki.invoices.domain.InvoiceStatus.PENDING
import com.gsuaki.invoices.repositories.InvoiceRepository
import com.gsuaki.invoices.repositories.tables.InvoiceEntity
import com.gsuaki.invoices.repositories.tables.InvoiceTable
import org.jetbrains.exposed.sql.and
import java.time.LocalDateTime

class DefaultInvoiceRepository : InvoiceRepository {

  override fun findAll(): Iterable<InvoiceEntity> = InvoiceEntity.all()

  override fun findByIdAndOwnerId(id: Long, ownerId: Long): InvoiceEntity? = InvoiceEntity
    .find { InvoiceTable.id eq id and (InvoiceTable.ownerId eq ownerId) }
    .firstOrNull()

  override fun save(entity: Invoice): InvoiceEntity = if (entity.id == null) {
    InvoiceEntity.new {
      ownerId = entity.ownerId
      status = PENDING
      createdAt = LocalDateTime.now()
    }
  } else {
    InvoiceEntity.findById(entity.id)?.apply {
      status = entity.status
      authorizedAt = entity.authorizedAt
    } ?: throw RuntimeException("")
  }
}