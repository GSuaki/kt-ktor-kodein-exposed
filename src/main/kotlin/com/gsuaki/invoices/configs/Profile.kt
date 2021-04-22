package com.gsuaki.invoices.configs

enum class Profile {
  TEST, DEV, STAGE, PROD;

  companion object {
    fun find(str: String): Profile? =
      values().find { it.name.equals(str, ignoreCase = true) }
  }
}