package com.gsuaki.invoices.configs

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun installMySQL(profile: Profile): Database {
  return HikariConfig("/hikari.${profile}.properties")
    .apply { schema = "invoices" }
    .let(::HikariDataSource)
    .let(Database::connect)
}

fun installMySQLLocal(url: String): Database {
  return Database.connect(
    url = url,
    driver = "com.mysql.cj.jdbc.MysqlDataSource",
    user = "root",
    password = "123456"
  )
}