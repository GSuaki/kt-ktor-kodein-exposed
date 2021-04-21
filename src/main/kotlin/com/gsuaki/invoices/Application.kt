package com.gsuaki.invoices

import com.gsuaki.invoices.configs.configure
import io.ktor.application.Application
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.start(dev: Boolean = false) = configure(dev)