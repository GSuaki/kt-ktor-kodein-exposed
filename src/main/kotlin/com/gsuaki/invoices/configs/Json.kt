package com.gsuaki.invoices.configs

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL
import com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.text.SimpleDateFormat

object Json {

  fun writeValueAsString(any: Any): String {
    return INSTANCE.writeValueAsString(any)
  }

  fun <T> readValue(data: ByteArray, type: Class<T>): T {
    return INSTANCE.readValue(data, type)
  }

  fun <T> readValue(data: ByteArray, type: TypeReference<T>): T {
    return INSTANCE.readValue(data, type)
  }

  fun getInstance(): ObjectMapper = INSTANCE.copy()

  private val INSTANCE = ObjectMapper().apply {
    disable(FAIL_ON_UNKNOWN_PROPERTIES)
    disable(WRITE_DATES_AS_TIMESTAMPS)

    enable(ACCEPT_CASE_INSENSITIVE_ENUMS)
    enable(WRITE_ENUMS_USING_TO_STRING)
    enable(READ_UNKNOWN_ENUM_VALUES_AS_NULL)

    registerModules(JavaTimeModule(), KotlinModule())

    dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
  }
}