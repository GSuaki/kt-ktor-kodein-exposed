package com.gsuaki.invoices.repositories

import com.gsuaki.invoices.domain.Invoice
import com.gsuaki.invoices.domain.InvoiceStatus
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.`java-time`.datetime as localDatetime

object InvoiceRepository : IdTable<Long>() {
  override val tableName: String
    get() = "invoices"

  override val id: Column<EntityID<Long>> = long("id").autoIncrement().primaryKey().entityId()
  val ownerId = long("owner_id")
  val status = enumerationByName("status", 255, InvoiceStatus::class)
  val createdAt = localDatetime("created_at")
  val authorizedAt = localDatetime("authorized_at").nullable()
}

class InvoiceEntity(id: EntityID<Long>) : LongEntity(id) {

  var ownerId by InvoiceRepository.ownerId
  var status by InvoiceRepository.status
  var createdAt by InvoiceRepository.createdAt
  var authorizedAt by InvoiceRepository.authorizedAt

  companion object : LongEntityClass<InvoiceEntity>(InvoiceRepository) {

    fun findByIdAndOwnerId(id: Long, ownerId: Long): InvoiceEntity? =
      find { InvoiceRepository.id eq id and (InvoiceRepository.ownerId eq ownerId) }
        .firstOrNull()
  }

  fun toDomain() = Invoice(
    id = id.value,
    ownerId = ownerId,
    status = status,
    createdAt = createdAt,
    authorizedAt = authorizedAt
  )
}