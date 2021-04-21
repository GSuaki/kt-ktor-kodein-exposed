package com.gsuaki.invoices.domain

import com.gsuaki.invoices.domain.InvoiceStatus.PENDING
import java.time.LocalDateTime

data class Invoice(
  val id: Long,
  val ownerId: Long,
  val status: InvoiceStatus = PENDING,
  val createdAt: LocalDateTime = LocalDateTime.now(),
  val authorizedAt: LocalDateTime? = null,
)