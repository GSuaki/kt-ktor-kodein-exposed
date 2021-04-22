package com.gsuaki.invoices.repositories

import com.gsuaki.invoices.domain.Invoice
import com.gsuaki.invoices.repositories.tables.InvoiceEntity

interface InvoiceRepository {

  fun findAll(): Iterable<InvoiceEntity>

  fun findByIdAndOwnerId(id: Long, ownerId: Long): InvoiceEntity?

  fun save(entity: Invoice): InvoiceEntity
}