# Spring and Spring Boot in Java for Web Applications

REST API for managing orders.

* **Tech:** Spring Boot 3.2, Java 17+, JPA (Hibernate 6)
* **DB:** H2 (dev/test), PostgreSQL (prod via Docker)
* **Build:** Maven (wrapper), multi-stage Dockerfile
* **Endpoints:** `/api/orders`

---

## Prerequisites

* **Java 17+** (local dev)
* **Docker Desktop** (for prod env)
* **PowerShell** (to run scripts on Windows) or run equivalent commands manually

---

## Quick start

### Dev (H2, in-memory)

```powershell
scripts\run-dev.ps1
```

* App: [http://localhost:8080](http://localhost:8080)
* H2 console (optional): [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

  * JDBC URL: `jdbc:h2:mem:ordersdb`
  * User: `sa`
  * Password: *(blank)*

### Prod (Docker: Postgres + app)

1. Create `.env` in project root:

```
POSTGRES_DB=orders
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
DDL_AUTO=update
DB_PORT=5433
```

2. Bring everything up:

```powershell
scripts\run-prod.ps1
```

* App: [http://localhost:8080](http://localhost:8080)
* Postgres exposed locally at `localhost:5433` → container `5432`

3. Check containers & logs:

```powershell
docker compose ps
docker logs -f orders-postgres
docker logs -f order-service
```

---

## API (CRUD)

Base path: `http://localhost:8080/api/orders`

```bash
# Create
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{ "notes":"First order", "status":"NEW", "total": 199.99 }' -i

# List
curl http://localhost:8080/api/orders

# Get by id
curl http://localhost:8080/api/orders/1

# Update
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{ "notes":"Updated", "status":"PAID", "total": 199.99 }' -i

# Delete
curl -X DELETE http://localhost:8080/api/orders/1 -i
```

---

## Swagger / OpenAPI

Auto-generated API docs are exposed by Springdoc.

* **Swagger UI (interactive):**

  * [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Project structure

```
src/
  main/
    java/orders/
      controller/   # REST endpoints
      service/      # application logic
      repo/         # Spring Data JPA repositories
      model/        # JPA entities
      dto/          # request/response DTOs
    resources/
      application.yml           # dev & prod profiles in one file
      application-test.yml      # test profile (H2, create-drop)
Dockerfile
docker-compose.yml
scripts/
  run-dev.ps1
  run-prod.ps1
  run-test.ps1
README.md
DECISIONS.md
postman/OrderService-Sprint1.postman_collection.json
```

---

## Configuration & Profiles

### Profiles in use

* **`dev`** (default): H2 in-memory, SQL formatting/logging on.
* **`test`**: H2 in-memory (isolated), schema `create-drop`.
* **`prod`**: PostgreSQL via environment variables (Docker).

### How profiles are selected

* **Dev**: `scripts\run-dev.ps1` runs `spring-boot:run` with `-Dspring-boot.run.profiles=dev`.
* **Prod**: `docker compose` sets env vars and runs the app with `SPRING_PROFILES_ACTIVE=prod` inside the container.
* **Test**: Tests annotate `@ActiveProfiles("test")` and load `src/test/resources/application-test.yml`.

### Environment variables (used by the app in prod)

* `DB_URL` (e.g. `jdbc:postgresql://postgres:5432/orders` **inside Docker**)
* `DB_USERNAME`
* `DB_PASSWORD`
* `DDL_AUTO` (default `update`)

> `docker-compose.yml` injects these from `.env` into the **app** container.
> Inside Compose, the app connects to host **`postgres`** (service name), not `localhost`.

### Database access from desktop tools (prod)

Use **DBeaver / IntelliJ Database plugin**:

* Host: `localhost`
* Port: `5433` (from `.env` → `DB_PORT`)
* Database: `orders`
* User: `postgres`
* Password: `postgres` (or whatever you set in `.env`)
* Driver: PostgreSQL

---

## Testing (JUnit) — `test` profile

* Unit + integration tests run under **`@ActiveProfiles("test")`** using H2 (`create-drop`).
* Run all tests:

```powershell
.\mvnw.cmd -q test
```

* Run from IntelliJ as usual.
* Optional coverage: add JaCoCo plugin if required by rubric.

---

## Postman collection

File: `postman/OrderService-Sprint1.postman_collection.json`

---

## Javadoc

Generate HTML Javadoc with the Maven **wrapper** (no global Maven required):

```powershell
.\mvnw.cmd javadoc:javadoc
```

Open: `target/site/apidocs/index.html`

> If the build fails on Javadoc, ensure public classes/methods have proper Javadoc and `@param/@return` tags where applicable.

---

## Decision log

Key decisions and justifications: **`DECISIONS.md`** (in repo root).
