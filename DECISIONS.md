## Context

This project delivers a minimal **Order** API using **Spring Boot 3**, **Java 17+**, and **JPA** with:

* **H2 (in-memory)** for development.
* **PostgreSQL** for production (via **Docker Compose**).
* Clean layering: `controller → service → repository → entity`.

## Key decisions (what & why)

1. **Database per environment**

    * **Dev = H2 (in-memory)**: fastest feedback loop, no local DB install, clean state on each run.
    * **Prod = PostgreSQL**: realistic RDBMS, persistent storage via Docker volume.

2. **Profiles & configuration**

    * `application.yml` uses **YAML documents** (---) with two profiles:

        * `dev`: H2 datasource, SQL pretty print, H2 console.
        * `prod`: Postgres datasource **injected via env vars** (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DDL_AUTO`).
    * Keep secrets/config out of source by using **`.env`** and Compose `environment` expansion.

3. **Build & containerization**

    * **Multi-stage Dockerfile**:

        * Stage 1: Maven build (cacheable deps, no tests for speed).
        * Stage 2: small Temurin 21 JRE image running the fat jar.
    * **Compose** orchestrates two services: `postgres` and `app`.
      `app` waits on DB health (`depends_on.condition: service_healthy`).

4. **Port mapping & client connectivity**

    * Host port **5433 → container 5432** to avoid conflicts with any local Postgres.
    * Inside the Compose network, the app connects via host **`postgres:5432`** (service name), **not** `localhost`.

5. **Startup scripts**

    * `scripts/run-dev.ps1`: runs **dev** profile locally with H2 (one line PowerShell; avoids env collisions).
    * `scripts/run-prod.ps1`: `docker compose --env-file .env up -d --build`, then prints service status.

6. **Domain model scope (MVP)**

    * `Order` fields: `id`, `notes`, `status`, `total`, `createdAt`.
    * CRUD only; no relations or aggregates yet (keeps Sprint 1 focused).

7. **API style**

    * RESTful controller at `/api/orders`.
    * Returns standard HTTP codes (201 with Location, 200/204/404).

8. **Documentation**

    * Javadoc on all public classes/methods.
    * README with run instructions, env vars, Postman collection, and troubleshooting.

