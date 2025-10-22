$ErrorActionPreference = "Stop"
docker compose --env-file .env up -d --build
docker compose ps
Write-Host "Stack up. App http://localhost:8080"
