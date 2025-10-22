
## Order Service (Spring Boot)

Minimal REST API for managing orders.

* **Tech:** Spring Boot 3.2, Java 17+, JPA (Hibernate 6)
* **DB:** H2 (dev), PostgreSQL (prod via Docker)
* **Build:** Maven, multi-stage Dockerfile
* **Endpoints:** `/api/orders`

---

## Prerequisites

* **Java 17+** (local dev)
* **Docker Desktop** (for prod env)
* **PowerShell** (scripts on Windows) or run equivalent commands manually

---

## Quick start

### Dev (H2, in-memory)

```powershell
scripts\run-dev.ps1
```

* App: [http://localhost:8080](http://localhost:8080)
* H2 console (if needed): [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  JDBC URL: `jdbc:h2:mem:ordersdb` (username: `sa`, password blank)

### Prod (Docker: Postgres + app)

1. Create `.env` in project root (example):

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
    * Postgres: `localhost:5433` (host) → container `5432` (internal)

3. Check containers:

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

## Project structure

```
src/
  main/
    java/orders/
      controller/  # REST endpoints
      service/     # business/application logic
      repo/        # Spring Data JPA repositories
      model/       # JPA entities
    resources/
      application.yml  # dev & prod profiles
Dockerfile
docker-compose.yml
scripts/
  run-dev.ps1
  run-prod.ps1
README.md
DECISIONS.md
postman/OrderService.postman_collection.json  # (recommend placing here)
```

---

## Configuration

### Profiles

* `dev` (default): H2 in-memory, SQL logging/formatting on.
* `prod`: PostgreSQL via env vars.

### Environment variables (used by app in prod)

* `DB_URL` (e.g. `jdbc:postgresql://postgres:5432/orders` **inside Docker**)
* `DB_USERNAME`
* `DB_PASSWORD`
* `DDL_AUTO` (default `update`)

> Compose sets these from `.env` and injects them into the app container.
> The app connects to host `postgres` (service name), not `localhost`.

### Database access from desktop tools (prod)

* **DBeaver / IntelliJ Database plugin**

    * Host: `localhost`
    * Port: `5433` (from `.env` → `DB_PORT`)
    * Database: `orders`
    * User: `postgres`
    * Password: `postgres` (or your `.env` value)
    * Driver: PostgreSQL

---

## Postman collection

JSON file at `postman/OrderService-Sprint1.postman_collection.json`.

---

## Javadoc

How to generate HTML Javadoc:

```bash
# plain
mvn javadoc:javadoc
```

Open: `target/site/apidocs/index.html`

