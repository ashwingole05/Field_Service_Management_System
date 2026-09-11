# Keystone Field Service Management

Keystone is a full-stack field service management platform for coordinating customers, sites, service requests, work orders, technicians, inventory, time logs, SLA tracking, and operational dashboards.

The application is designed to run as a single deployed service: the React frontend is built into the Spring Boot backend and both the UI and API are served from one URL.

## Features

- JWT-based authentication and role-based authorization
- Customer, site, service request, and work order management
- Technician assignment and work order lifecycle tracking
- Parts inventory and work order part usage
- Time logging for technician work
- SLA reporting and dashboard analytics
- One-url Docker deployment for frontend and backend

## Tech Stack

| Area | Technology |
| --- | --- |
| Frontend | React, Vite, React Router, Ant Design, Tailwind CSS, Axios |
| Backend | Java 25, Spring Boot, Spring Security, Spring Data JPA |
| Database | PostgreSQL / NeonDB |
| Build and Deploy | Maven, npm, Docker |

##  Live Deployment

**Live Application:**  
[https://keystone-fieldservice.onrender.com](https://keystone-fieldservice.onrender.com)

**Health Check:**  
[https://keystone-fieldservice.onrender.com/actuator/health](https://keystone-fieldservice.onrender.com/actuator/health)

## Architecture

```text
com.FieldService/
+-- Dockerfile                  # Production Docker build for frontend + backend
+-- .dockerignore
+-- README.md
+-- backend/
|   +-- pom.xml
|   +-- src/main/java/com/FieldService/
|   |   +-- Controller/
|   |   +-- DTO/
|   |   +-- Entity/
|   |   +-- Repository/
|   |   +-- Security/
|   |   +-- Service/
|   +-- src/main/resources/
|       +-- application.properties
+-- frontend/
    +-- package.json
    +-- vite.config.js
    +-- src/
```

Production flow:

1. Docker builds the React app from `frontend`.
2. The generated `frontend/dist` files are copied into `backend/src/main/resources/static`.
3. Maven packages the Spring Boot application.
4. Spring Boot serves both frontend routes and backend APIs from the same origin.

## Environment Variables

Create a `.env` file for localhost Docker usage or configure these variables in your live hosting provider.

```env
DATABASE_URL=jdbc:postgresql://YOUR_NEON_HOST/YOUR_DATABASE?sslmode=require
DATABASE_USERNAME=YOUR_NEON_USERNAME
DATABASE_PASSWORD=YOUR_NEON_PASSWORD

JWT_SECRET=your_64_character_secret
JWT_EXPIRATION_MS=43200000

MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password

PORT=10000
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

Notes:

- `PORT` defaults to `8081` in Spring Boot and `10000` in the Docker image.
- `CORS_ALLOWED_ORIGINS` is mainly needed during local split frontend/backend development.
- In one-url deployment, frontend API requests use relative `/api/...` paths.

## Local Development

Use this setup when working on the application locally.

### Run Services Separately

Run the backend and frontend separately for day-to-day development.

Start the backend:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

On macOS/Linux, use:

```bash
cd backend
./mvnw spring-boot:run
```

Backend URL:

```text
http://localhost:8081
```

Health check:

```http
GET http://localhost:8081/actuator/health
```

Start the frontend in a separate terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

During local development, the Vite dev server proxies `/api` and `/actuator` requests to `http://localhost:8081`. You can also set `VITE_API_URL=http://localhost:8081` if you want an explicit frontend API base URL.

### Run With Docker

Build and run the complete application from one localhost URL:

```bash
docker build -t keystone-fieldservice .
docker run -p 10000:10000 --env-file .env keystone-fieldservice
```

Open:

```text
http://localhost:10000
```

The root `Dockerfile` is the deployment Dockerfile. Do not build from the `backend` directory.

## Production Deployment

Use this deployment configuration for Render or any Docker-based hosting platform.

Build settings:

| Setting | Value |
| --- | --- |
| Build context | Repository root |
| Dockerfile | `Dockerfile` |
| Runtime port | `${PORT}` |
| Default container port | `10000` |

Required production environment variables:

```env
DATABASE_URL=jdbc:postgresql://YOUR_NEON_HOST/YOUR_DATABASE?sslmode=require
DATABASE_USERNAME=YOUR_NEON_USERNAME
DATABASE_PASSWORD=YOUR_NEON_PASSWORD
JWT_SECRET=your_64_character_secret
JWT_EXPIRATION_MS=43200000
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password
```

Example live URLs:

```text
https://keystone-fieldservice.onrender.com
https://keystone-fieldservice.onrender.com/login
https://keystone-fieldservice.onrender.com/dashboard
https://keystone-fieldservice.onrender.com/api/...
```

Health check:

```http
GET https://keystone-fieldservice.onrender.com/actuator/health
```

## Authentication

The backend uses JWT authentication. Protected API requests must include the token returned by the login endpoint:

```http
Authorization: Bearer <jwt_token>
```

Login:

```http
POST /api/user_auth/login
```

```json
{
  "userEmail": "manager@example.com",
  "password": "password123"
}
```

## Roles

| Role | Access |
| --- | --- |
| `MANAGER` | Full system access, user management, reports, dashboards, request review, and work order management |
| `DISPATCHER` | Customer/site management, service request review, work order creation, assignment, cancellation, and closure |
| `TECHNICIAN` | Assigned work orders, work progress updates, time logs, and part usage |
| `CUSTOMER` | Own sites and service requests |

## Role-Based REST API Usage

All protected requests require a bearer token:

```http
Authorization: Bearer <jwt_token>
```

Use the same API paths for localhost and production. Only the base URL changes:

```text
Local backend: http://localhost:8081
Local Docker:  http://localhost:10000
Production:    https://keystone-fieldservice.onrender.com
```

### Manager API Structure

Managers have full operational access. A typical manager flow is to create staff users, manage customers and sites, create or assign work orders, manage inventory, and review dashboard/SLA reports.

| Action | Method | Endpoint |
| --- | --- | --- |
| Create staff user | `POST` | `/api/user_auth/staff` |
| View staff users | `GET` | `/api/user_auth/staff` |
| Manage customers | `POST`, `GET`, `PUT`, `DELETE` | `/api/customers` |
| Manage sites | `POST`, `GET`, `PUT`, `DELETE` | `/api/sites` |
| Manage work orders | `POST`, `GET`, `PUT`, `DELETE` | `/api/workorders` |
| Assign technician | `PUT` | `/api/workorders/{id}/assign/{technicianId}` |
| Manage parts | `POST`, `GET`, `PUT`, `DELETE` | `/api/parts` |
| View dashboard | `GET` | `/api/dashboard` |
| View SLA reports | `GET` | `/api/sla/overdue` |

Create a technician:

```http
POST /api/user_auth/staff
Authorization: Bearer <manager_jwt_token>
Content-Type: application/json
```

```json
{
  "userName": "Technician One",
  "userEmail": "tech@example.com",
  "phone": "9876543210",
  "password": "password123",
  "role": "TECHNICIAN"
}
```

Create a work order:

```http
POST /api/workorders
Authorization: Bearer <manager_jwt_token>
Content-Type: application/json
```

```json
{
  "title": "Repair AC unit",
  "description": "Inspect and repair the AC unit on the first floor",
  "priority": "HIGH",
  "status": "OPEN",
  "siteId": 1,
  "assignedTechnicianId": 3,
  "scheduledAt": "2026-09-15T10:00:00"
}
```

### Dispatcher API Structure

Dispatchers handle day-to-day service operations. They can maintain customers and sites, review service requests, convert requests into work orders, and assign technicians.

| Action | Method | Endpoint |
| --- | --- | --- |
| Create customer | `POST` | `/api/customers` |
| Create site | `POST` | `/api/sites` |
| Review service requests | `GET` | `/api/service-requests` |
| Mark request in review | `PUT` | `/api/service-requests/{id}/review` |
| Convert request to work order | `POST` | `/api/service-requests/{id}/convert` |
| Create work order | `POST` | `/api/workorders` |
| Assign technician | `PUT` | `/api/workorders/{id}/assign/{technicianId}` |
| Close work order | `PUT` | `/api/workorders/{id}/close` |

Create a customer:

```http
POST /api/customers
Authorization: Bearer <dispatcher_jwt_token>
Content-Type: application/json
```

```json
{
  "companyName": "ABC Industries",
  "contactPerson": "Ashwin",
  "email": "customer@example.com",
  "phone": "9876543210",
  "address": "12 Industrial Road",
  "city": "Chennai",
  "state": "Tamil Nadu",
  "postalCode": "600001"
}
```

Convert a service request to a work order:

```http
POST /api/service-requests/5/convert
Authorization: Bearer <dispatcher_jwt_token>
```

### Technician API Structure

Technicians work on assigned jobs. They can view work orders, update work progress, log time, view parts, and record parts used on a work order.

| Action | Method | Endpoint |
| --- | --- | --- |
| View work orders | `GET` | `/api/workorders` |
| View assigned work orders | `GET` | `/api/workorders/technician/{technicianId}` |
| Accept work order | `PUT` | `/api/workorders/{id}/accept` |
| Start work | `PUT` | `/api/workorders/{id}/start` |
| Hold work | `PUT` | `/api/workorders/{id}/hold` |
| Resume work | `PUT` | `/api/workorders/{id}/resume` |
| Complete work | `PUT` | `/api/workorders/{id}/complete` |
| Start time log | `POST` | `/api/time-logs/start` |
| Stop time log | `PUT` | `/api/time-logs/{id}/stop` |
| Use part | `POST` | `/api/workorder-parts/use` |

Start a work order:

```http
PUT /api/workorders/10/start
Authorization: Bearer <technician_jwt_token>
```

Start a time log:

```http
POST /api/time-logs/start
Authorization: Bearer <technician_jwt_token>
Content-Type: application/json
```

```json
{
  "workOrderId": 10,
  "technicianId": 3,
  "notes": "Started diagnosis"
}
```

Record part usage:

```http
POST /api/workorder-parts/use
Authorization: Bearer <technician_jwt_token>
Content-Type: application/json
```

```json
{
  "workOrderId": 10,
  "partId": 2,
  "quantityUsed": 1
}
```

### Customer API Structure

Customers can register, manage their own sites, raise service requests, and view their own requests.

| Action | Method | Endpoint |
| --- | --- | --- |
| Register customer user | `POST` | `/api/user_auth/register` |
| View own profile | `GET` | `/api/user_auth/me` |
| Create site | `POST` | `/api/sites` |
| View own sites | `GET` | `/api/sites/mine` |
| Raise service request | `POST` | `/api/service-requests` |
| View own service requests | `GET` | `/api/service-requests/mine` |

Register a customer:

```http
POST /api/user_auth/register
Content-Type: application/json
```

```json
{
  "userName": "Customer One",
  "userEmail": "customer@example.com",
  "phone": "9876543210",
  "companyName": "ABC Industries",
  "password": "password123"
}
```

Raise a service request:

```http
POST /api/service-requests
Authorization: Bearer <customer_jwt_token>
Content-Type: application/json
```

```json
{
  "siteId": 1,
  "title": "AC not cooling",
  "description": "The AC unit on the first floor is not cooling properly"
}
```

## Core Workflow

1. Create the initial manager with `/api/user_auth/setup-manager`.
2. Login as manager.
3. Create dispatcher and technician users.
4. Register or create a customer.
5. Create customer sites.
6. Raise a service request.
7. Review the request as dispatcher or manager.
8. Convert the request into a work order.
9. Assign the work order to a technician.
10. Track technician progress, time logs, and part usage.
11. Complete and close the work order.
12. Review dashboards and SLA reports.

## API Reference

Base URL:

```text
Local backend: http://localhost:8081
Local Docker:  http://localhost:10000
Live:          https://keystone-fieldservice.onrender.com
```

### Public Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/user_auth/register` | Register a customer user |
| `POST` | `/api/user_auth/login` | Login user |
| `POST` | `/api/user_auth/setup-manager` | Create initial manager |
| `POST` | `/api/user_auth/logout` | Logout current token |
| `POST` | `/api/user_auth/forgetPassword` | Send password reset email |
| `POST` | `/api/user_auth/resetPassword` | Reset user password |
| `POST` | `/api/email_log/resetPasswordEmail` | Send reset password email |
| `POST` | `/api/email_log/notify` | Send notification email |
| `GET` | `/actuator/health` | Application health check |

### User Auth

| Method | Endpoint | Access |
| --- | --- | --- |
| `GET` | `/api/user_auth/me` | Authenticated user |
| `GET` | `/api/user_auth/technicians` | `VIEW_USER`, `ASSIGN_WO` |
| `GET` | `/api/user_auth/staff` | `VIEW_USER` |
| `POST` | `/api/user_auth/staff` | `CREATE_USER` |

Create staff user body:

```json
{
  "userName": "Technician One",
  "userEmail": "tech@example.com",
  "phone": "9876543210",
  "password": "password123",
  "role": "TECHNICIAN"
}
```

### Customers

| Method | Endpoint | Permission |
| --- | --- | --- |
| `POST` | `/api/customers` | `CREATE_CUSTOMER` |
| `GET` | `/api/customers` | `VIEW_CUSTOMER` |
| `GET` | `/api/customers/{id}` | `VIEW_CUSTOMER` |
| `PUT` | `/api/customers/{id}` | `UPDATE_CUSTOMER` |
| `DELETE` | `/api/customers/{id}` | `DELETE_CUSTOMER` |

### Sites

| Method | Endpoint | Permission |
| --- | --- | --- |
| `POST` | `/api/sites` | `CREATE_SITE` |
| `GET` | `/api/sites` | `VIEW_SITE` |
| `GET` | `/api/sites/mine` | Authenticated user |
| `GET` | `/api/sites/customer/{customerId}` | `VIEW_SITE` |
| `GET` | `/api/sites/{id}` | `VIEW_SITE` |
| `PUT` | `/api/sites/{id}` | `UPDATE_SITE` |
| `DELETE` | `/api/sites/{id}` | `DELETE_SITE` |

### Service Requests

| Method | Endpoint | Permission |
| --- | --- | --- |
| `POST` | `/api/service-requests` | `RAISE_REQUEST` |
| `GET` | `/api/service-requests/mine` | `VIEW_OWN_REQUEST` |
| `GET` | `/api/service-requests` | `REVIEW_REQUEST` |
| `GET` | `/api/service-requests/customer/{customerId}` | `REVIEW_REQUEST` |
| `GET` | `/api/service-requests/status/{status}` | `REVIEW_REQUEST` |
| `GET` | `/api/service-requests/{id}` | `REVIEW_REQUEST` |
| `PUT` | `/api/service-requests/{id}/review` | `REVIEW_REQUEST` |
| `POST` | `/api/service-requests/{id}/convert` | `CONVERT_REQUEST` |
| `PUT` | `/api/service-requests/{id}/close` | `CLOSE_REQUEST` |
| `PUT` | `/api/service-requests/{id}/cancel` | `CANCEL_REQUEST` |

Statuses:

```text
OPEN
IN_REVIEW
CONVERTED_TO_WORK_ORDER
CLOSED
CANCELLED
```

### Work Orders

| Method | Endpoint | Permission |
| --- | --- | --- |
| `POST` | `/api/workorders` | `CREATE_WO` |
| `GET` | `/api/workorders` | `VIEW_WO` |
| `GET` | `/api/workorders/{id}` | `VIEW_WO` |
| `PUT` | `/api/workorders/{id}` | `UPDATE_WO` |
| `DELETE` | `/api/workorders/{id}` | `DELETE_WO` |
| `GET` | `/api/workorders/site/{siteId}` | `VIEW_WO` |
| `GET` | `/api/workorders/technician/{technicianId}` | `VIEW_WO` |
| `GET` | `/api/workorders/status/{status}` | `VIEW_WO` |
| `PUT` | `/api/workorders/{id}/assign/{technicianId}` | `ASSIGN_WO` |
| `PUT` | `/api/workorders/{id}/accept` | `START_WORK` |
| `PUT` | `/api/workorders/{id}/start` | `START_WORK` |
| `PUT` | `/api/workorders/{id}/hold` | `HOLD_WORK` |
| `PUT` | `/api/workorders/{id}/resume` | `RESUME_WORK` |
| `PUT` | `/api/workorders/{id}/complete` | `COMPLETED_WORK` |
| `PUT` | `/api/workorders/{id}/cancel` | `CANCEL_WO` |
| `PUT` | `/api/workorders/{id}/close` | `CLOSE_WO` |

Statuses:

```text
OPEN
ASSIGNED
ACCEPTED
IN_PROGRESS
ON_HOLD
COMPLETED
CANCELLED
CLOSED
```

Priorities:

```text
LOW
MEDIUM
HIGH
CRITICAL
```

### Parts

| Method | Endpoint | Permission |
| --- | --- | --- |
| `POST` | `/api/parts` | `ADD_PARTS` |
| `GET` | `/api/parts` | `VIEW_PARTS` |
| `GET` | `/api/parts/{id}` | `VIEW_PARTS` |
| `PUT` | `/api/parts/{id}` | `UPDATE_PARTS` |
| `PUT` | `/api/parts/{id}/stock/{quantity}` | `UPDATE_PARTS` |
| `DELETE` | `/api/parts/{id}` | `DELETE_PART` |

### Work Order Parts

| Method | Endpoint | Permission |
| --- | --- | --- |
| `POST` | `/api/workorder-parts/use` | `USE_PARTS` |
| `GET` | `/api/workorder-parts` | `VIEW_PARTS` |
| `GET` | `/api/workorder-parts/workorder/{workOrderId}` | `VIEW_PARTS` |

### Time Logs

| Method | Endpoint | Permission |
| --- | --- | --- |
| `POST` | `/api/time-logs/start` | `ADD_LOG_TIME` |
| `PUT` | `/api/time-logs/{id}/stop` | `ADD_LOG_TIME` |
| `GET` | `/api/time-logs` | `VIEW_LOG_TIME` |
| `GET` | `/api/time-logs/workorder/{workOrderId}` | `VIEW_LOG_TIME` |
| `GET` | `/api/time-logs/technician/{technicianId}` | `VIEW_LOG_TIME` |

### Dashboard and SLA

| Method | Endpoint | Permission |
| --- | --- | --- |
| `GET` | `/api/dashboard` | `VIEW_DASHBOARD` |
| `GET` | `/api/sla/overdue` | `VIEW_REPORTS` |
| `GET` | `/api/sla/overdue/count` | `VIEW_REPORTS` |

## Verification

Backend package:

```bash
cd backend
./mvnw -DskipTests package
```

Frontend production build:

```bash
cd frontend
npm run build
```

> [!NOTE]
> Render free or low-traffic services may take extra time to respond after inactivity while the instance starts again. The first request can be slower; later requests should respond normally.

## Operational Notes

- Keep database, mail, and JWT secrets out of source control.
- Use the repository root as the Docker build context.
- The root `Dockerfile` is the only Dockerfile required for deployment.
- Local development can run as two services; Docker and live deployment run as one service.
