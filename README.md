# kt-ktor-kodein-exposed
Real example of RESTful Service using Kotlin, Ktor, Kodein, Arrow-Kt, and Exposed with HikariCP, H2 &amp; MySQL

## Specs

### Web Framework
- [Ktor.io](https://ktor.io/)
- [CIO Engine](https://ktor.io/docs/engines.html#dependencies)

### DI Framework
- [kodein](https://kodein.org/)

**Why?**
- [Koin vs Kodein - DI with Kotlin](https://www.kotlindevelopment.com/koin-vs-kodein/)

### Database
- [Exposed](https://github.com/JetBrains/Exposed)
- [Testcontainers](https://www.testcontainers.org/)

**Running locally**
1. Start Docker on your machine.

2. Execute mysql_run script:
```
sudo ./scripts/mysql_run.sh
```

3. Execute initial SQL scripts by running:
```
sudo ./scripts/init_local.sh 
```