# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Structure

The actual Spring Boot project lives in the `backend-practice/` subdirectory (not the repo root). The repo root contains a stale Gradle setup that is not used — ignore it.

## Build & Run Commands

All commands should be run from the `backend-practice/` directory.

```bash
# Build (skip tests)
./mvnw package -DskipTests

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=AuthServiceImplTest

# Start the application (auto-starts PostgreSQL via Docker Compose)
./mvnw spring-boot:run
```

## Prerequisites

- Java 17
- Docker (required — Spring Boot Docker Compose support auto-starts a PostgreSQL container from `compose.yaml`)

## Tech Stack

- **Spring Boot 4.0.3** with Maven, Java 17, Lombok
- **Spring Security** — stateless JWT auth (jjwt 0.12.6), BCrypt passwords
- **Spring Data JPA** — PostgreSQL, Hibernate `create-drop` DDL mode
- **Swagger UI** — Springdoc OpenAPI at `/swagger-ui/`

## Architecture

Standard layered Spring Boot REST API: Controller → Service (interface + impl) → Repository → Entity.

- **Entities:** `Member` ↔ `Post` — bidirectional `@OneToMany`/`@ManyToOne` with lazy fetch. On member deletion, `@PreRemove` nullifies the member reference on posts (posts are preserved).
- **JWT flow:** `JwtTokenProvider` creates/validates tokens → `JwtAuthenticationFilter` extracts from `Authorization: Bearer <token>` header → sets Spring Security context. Public endpoints: `/auth/login`, `/auth/signup`, Swagger paths.
- **Authorization:** Owner-only checks for member updates/deletes and post edits/deletes are done in service implementations, not via Spring Security method-level annotations.
- **DTOs:** All request/response objects are in `controller.dto` package — controllers never expose entities directly.

## Key Conventions

- Package: `org.example.backendpractice`
- Service layer uses interface + `*Impl` pattern
- Lombok is used throughout (`@Getter`, `@Setter`, `@Builder`, `@RequiredArgsConstructor`, `@NoArgsConstructor`)
- Code comments are written in Korean
