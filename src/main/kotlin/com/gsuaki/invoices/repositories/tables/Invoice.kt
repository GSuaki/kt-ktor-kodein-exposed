package com.gsuaki.invoices.repositories.tables

import com.gsuaki.invoices.domain.Invoice
import com.gsuaki.invoices.domain.InvoiceStatus
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.datetime as localDatetime

object InvoiceTable : IdTable<Long>() {
  override val tableName: String
    get() = "invoices"

  override val id: Column<EntityID<Long>> = long("id").autoIncrement().primaryKey().entityId()
  val ownerId = InvoiceTable.long("owner_id")
  val status = InvoiceTable.enumerationByName("status", 255, InvoiceStatus::class)
  val createdAt = localDatetime("created_at")
  val authorizedAt = localDatetime("authorized_at").nullable()
}

class InvoiceEntity(id: EntityID<Long>) : LongEntity(id) {

  var ownerId by InvoiceTable.ownerId
  var status by InvoiceTable.status
  var createdAt by InvoiceTable.createdAt
  var authorizedAt by InvoiceTable.authorizedAt

  companion object : LongEntityClass<InvoiceEntity>(InvoiceTable)

  fun toDomain() = Invoice(
    id = id.value,
    ownerId = ownerId,
    status = status,
    createdAt = createdAt,
    authorizedAt = authorizedAt
  )
}