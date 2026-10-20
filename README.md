# autoplatz-platform

Generated with forja. API in **Spring Boot** (Java 21), **hexagonal** architecture, web app in
**Next.js 16**.

## First 5 minutes

Requirements: Java 21, Node 22, Docker.

```bash
docker compose up -d                 # PostgreSQL
npm install
npm run dev:api                      # API at http://localhost:8080 (Swagger: /swagger-ui.html)
npm run dev:web                      # web app at http://localhost:3000
```

## Tests

```bash
npm test                             # API (unit, use cases, API, real startup, ArchUnit) + web
```

The API tests use a real PostgreSQL (Testcontainers, Docker).

## Structure

```
apps/api/src/main/java/com/autoplatz/
  shared/                 domain errors, Problem Details (RFC 9457), current user
  tasks/                  example module
    domain/               Task, TaskTitle — pure Java
    application/          use cases + ports (application/port)
    infrastructure/       adapters: web (REST), persistence, config (wiring)
apps/web/                 Next.js: pages, API client
packages/shared/          types shared by the web app
```

See [`docs/architecture.md`](docs/architecture.md): layers, how to add a module and what each test checks.

## Specification (Specgate)

The project is ready for spec-driven development with
[Specgate](https://www.npmjs.com/package/@rsaglobaltech/specgate): the client brief goes in
[`docs/brief.md`](docs/brief.md) and the gate (`npx @rsaglobaltech/specgate check`) runs `npm test`. See
[`docs/specgate.md`](docs/specgate.md).
