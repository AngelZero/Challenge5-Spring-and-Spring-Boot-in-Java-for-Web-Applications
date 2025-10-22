$ErrorActionPreference = "Stop"

# 1) Prevent conflict with docker
try { docker compose stop app | Out-Null } catch {}

# 2) Start dev profile
.\mvnw.cmd --% spring-boot:run -Dspring-boot.run.profiles=dev
