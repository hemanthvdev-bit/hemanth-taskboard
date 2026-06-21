# Taskboard — Full Stack Task Management

A full stack task management application built with Spring Boot and React. Users register, authenticate via JWT, and manage personal tasks with status tracking, priority levels, and due dates.

## Stack

**Backend**
- Java 17, Spring Boot 3.2
- Spring Security with stateless JWT authentication
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway for schema migrations
- Maven

**Frontend**
- React 18 (Vite)
- React Router for client-side routing
- Axios with request/response interceptors for token handling
- Plain CSS (no framework)

## Features

- User registration and login with BCrypt password hashing
- JWT-based stateless auth (token issued on login, validated per-request via filter)
- Full CRUD on tasks, scoped per-user (a user can only see/edit their own tasks)
- Task status (To Do / In Progress / Done) and priority (Low / Medium / High)
- Filtering by status, pagination on the task list endpoint
- Task stats summary (counts per status)
- Centralized exception handling with consistent JSON error responses
- Bean validation on all request DTOs

## Project Structure

```
taskboard/
├── backend/
│   └── src/main/java/com/hemanth/taskboard/
│       ├── controller/      REST endpoints
│       ├── service/         business logic
│       ├── repository/      Spring Data JPA repositories
│       ├── model/            JPA entities
│       ├── dto/              request/response objects
│       ├── security/         JWT filter, token provider, UserDetails impl
│       ├── config/           Spring Security config
│       └── exception/        global exception handling
└── frontend/
    └── src/
        ├── api/               axios client + endpoint wrappers
        ├── components/        TaskCard, TaskForm, Navbar
        ├── context/           AuthContext (JWT-aware)
        ├── hooks/             useTasks
        └── pages/             Login, Register, Dashboard
```

## Running locally

### Backend

Requires Java 17+, Maven, and a running PostgreSQL instance.

```bash
createdb taskboard
cd backend
cp .env.example .env   # then export the values or set them in your shell
mvn spring-boot:run
```

The API runs on `http://localhost:8080`. Flyway applies the schema automatically on startup.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The dev server runs on `http://localhost:5173` and proxies `/api` requests to the backend.

## API overview

| Method | Endpoint              | Description                  | Auth required |
|--------|------------------------|-------------------------------|----------------|
| POST   | `/api/auth/register`   | Create account, returns JWT  | No             |
| POST   | `/api/auth/login`      | Authenticate, returns JWT    | No             |
| GET    | `/api/tasks`           | List tasks (paginated, filterable by `status`) | Yes |
| GET    | `/api/tasks/{id}`      | Get a single task            | Yes            |
| POST   | `/api/tasks`           | Create a task                | Yes            |
| PUT    | `/api/tasks/{id}`      | Update a task                | Yes            |
| DELETE | `/api/tasks/{id}`      | Delete a task                | Yes            |
| GET    | `/api/tasks/stats`     | Status counts for current user | Yes          |

All authenticated requests need `Authorization: Bearer <token>`.

## Testing

```bash
cd backend
mvn test
```

Service-layer tests use Mockito; an H2 in-memory profile is configured for any future repository/integration tests.

## Notes

This started as a FastAPI + React version and was rebuilt on Spring Boot to match a Java-first backend stack — same product scope, different language and framework choices (layered controller/service/repository architecture, JPA instead of an ORM-light approach, JWT auth via a Spring Security filter chain instead of FastAPI's dependency injection).
