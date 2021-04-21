package integration

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.MySQLContainerProvider
import org.testcontainers.containers.wait.strategy.Wait
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class MySqlExtension : BeforeAllCallback, AfterAllCallback {
  override fun beforeAll(p0: ExtensionContext?) {
    if (!isDatabaseCreated.getAndSet(true)) {
      val dbContainer = MySQLContainerProvider().newInstance("8.0.20")
        .withExposedPorts(MYSQL_PORT)
        .withDatabaseName("invoices")
        .withUsername("root")
        .withPassword("123456")
        .withReuse(true)
        .waitingFor(Wait.forLogMessage("Container is started (JDBC URL: ", 1))
        .apply { start() }


      CONTAINER.set(dbContainer)
    }
  }

  override fun afterAll(p0: ExtensionContext?) {
    CONTAINER.get().stop()
  }

  companion object {
    private const val MYSQL_PORT = 3306
    private val isDatabaseCreated = AtomicBoolean(false)

    val CONTAINER = AtomicReference<JdbcDatabaseContainer<*>>()
  }
}