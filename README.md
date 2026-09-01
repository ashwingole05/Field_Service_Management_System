# Keystone Field Service Management

Keystone is a full-stack Field Service Management application for managing customers, sites, service requests, work orders, technicians, parts, time logs, SLA reports, and dashboard analytics.

## 1. Tech Stack

### 1.1 Backend

1. Java 25
2. Spring Boot
3. Spring Security
4. JWT Authentication
5. Spring Data JPA
6. PostgreSQL / NeonDB

### 1.2 Frontend

1. React
2. Vite
3. Ant Design
4. Tailwind CSS
5. Axios
6. React Router

## 2. Project Structure

```text
com.FieldService/
+-- backend/
|   +-- pom.xml
|   +-- src/main/java/com/FieldService/
|       +-- Controller/
|       +-- DTO/
|       +-- Entity/
|       +-- Repository/
|       +-- Security/
|       +-- Service/
|
+-- frontend/
    +-- package.json
    +-- .env
    +-- src/
```

## 3. Local Setup

### 3.1 Backend Setup

Go to the backend folder:

```bash
cd backend
```

Add the required environment variables:

```env
DATABASE_URL=jdbc:postgresql://YOUR_NEON_HOST/YOUR_DATABASE?sslmode=require
DATABASE_USERNAME=YOUR_NEON_USERNAME
DATABASE_PASSWORD=YOUR_NEON_PASSWORD
PORT=8081
JWT_SECRET=your_64_character_secret
JWT_EXPIRATION_MS=43200000
CORS_ALLOWED_ORIGINS=http://localhost:5173
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password
```

Run backend on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Run backend on macOS/Linux:

```bash
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

### 3.2 Frontend Setup

Go to the frontend folder:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Create or update `.env`:

```env
VITE_API_URL=http://localhost:8081
```

Run frontend:

```bash
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## 4. Authentication

The backend uses JWT authentication.

After login, pass the token in every protected API request:

```http
Authorization: Bearer <jwt_token>
```

### 4.1 Login Request

```http
POST /api/user_auth/login
```

```json
{
  "userEmail": "manager@example.com",
  "password": "password123"
}
```

### 4.2 Login Response

```json
{
  "token": "jwt-token",
  "message": "Login successful"
}
```

## 5. Roles

| Role | Description |
| --- | --- |
| MANAGER | Full access to the system |
| DISPATCHER | Handles customers, sites, service requests, and work orders |
| TECHNICIAN | Works on assigned work orders, logs time, and uses parts |
| CUSTOMER | Creates sites and raises service requests |

## 6. Role Permissions

### 6.1 Manager

Manager has all permissions.

Manager can:

1. Manage users
2. Manage customers
3. Manage sites
4. Manage work orders
5. Manage parts
6. View dashboard
7. View SLA reports
8. Review and convert service requests

### 6.2 Dispatcher

Dispatcher can:

1. Create, update, and view customers
2. Create, update, and view sites
3. Create, update, view, assign, cancel, and close work orders
4. View dashboard
5. Review service requests
6. Convert service requests to work orders
7. Close or cancel service requests

### 6.3 Technician

Technician can:

1. View work orders
2. Accept assigned work
3. Start work
4. Hold work
5. Resume work
6. Complete work
7. View parts
8. Use parts
9. Add time logs
10. View time logs

### 6.4 Customer

Customer can:

1. Create site
2. Update site
3. Raise service request
4. View own service requests

## 7. API Base URL

For local development:

```text
http://localhost:8081
```

For live deployment, use the deployed backend URL.

## 8. Public APIs

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/user_auth/register` | Register a customer user |
| POST | `/api/user_auth/login` | Login user |
| POST | `/api/user_auth/setup-manager` | Create initial manager |
| POST | `/api/user_auth/logout` | Logout current token |
| POST | `/api/user_auth/forgetPassword` | Send password reset email |
| POST | `/api/user_auth/resetPassword` | Reset user password |
| POST | `/api/email_log/resetPasswordEmail` | Send reset password email |
| POST | `/api/email_log/notify` | Send notification email |
| GET | `/actuator/health` | Backend health check |

## 9. Auth APIs

### 9.1 Register Customer

```http
POST /api/user_auth/register
```

Request body:

```json
{
  "userName": "Ashwin",
  "userEmail": "customer@example.com",
  "phone": "9876543210",
  "companyName": "ABC Industries",
  "password": "password123"
}
```

### 9.2 Login

```http
POST /api/user_auth/login
```

Request body:

```json
{
  "userEmail": "manager@example.com",
  "password": "password123"
}
```

### 9.3 Get Current User

```http
GET /api/user_auth/me
```

Requires:

```http
Authorization: Bearer <jwt_token>
```

### 9.4 Get Technicians

```http
GET /api/user_auth/technicians
```

Allowed permissions:

```text
VIEW_USER
ASSIGN_WO
```

### 9.5 Get Staff Users

```http
GET /api/user_auth/staff
```

Allowed permission:

```text
VIEW_USER
```

### 9.6 Create Staff User

```http
POST /api/user_auth/staff
```

Allowed permission:

```text
CREATE_USER
```

Request body:

```json
{
  "userName": "Technician One",
  "userEmail": "tech@example.com",
  "phone": "9876543210",
  "password": "password123",
  "role": "TECHNICIAN"
}
```

### 9.7 Forgot Password

```http
POST /api/user_auth/forgetPassword?userEmail=user@example.com
```

### 9.8 Reset Password

```http
POST /api/user_auth/resetPassword?token=reset-token&newPassword=newPassword123
```

## 10. Customer APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| POST | `/api/customers` | CREATE_CUSTOMER |
| GET | `/api/customers` | VIEW_CUSTOMER |
| GET | `/api/customers/{id}` | VIEW_CUSTOMER |
| PUT | `/api/customers/{id}` | UPDATE_CUSTOMER |
| DELETE | `/api/customers/{id}` | DELETE_CUSTOMER |

Request body:

```json
{
  "companyName": "ABC Industries",
  "contactPerson": "Ashwin",
  "email": "customer@example.com",
  "phone": "9876543210",
  "address": "Street address",
  "city": "Chennai",
  "state": "Tamil Nadu",
  "postalCode": "600001"
}
```

## 11. Site APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| POST | `/api/sites` | CREATE_SITE |
| GET | `/api/sites` | VIEW_SITE |
| GET | `/api/sites/mine` | Authenticated user |
| GET | `/api/sites/customer/{customerId}` | VIEW_SITE |
| GET | `/api/sites/{id}` | VIEW_SITE |
| PUT | `/api/sites/{id}` | UPDATE_SITE |
| DELETE | `/api/sites/{id}` | DELETE_SITE |

Request body:

```json
{
  "siteName": "Main Office",
  "address": "Street address",
  "city": "Chennai",
  "state": "Tamil Nadu",
  "pincode": "600001",
  "customerId": 1
}
```

## 12. Service Request APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| POST | `/api/service-requests` | RAISE_REQUEST |
| GET | `/api/service-requests/mine` | VIEW_OWN_REQUEST |
| GET | `/api/service-requests` | REVIEW_REQUEST |
| GET | `/api/service-requests/customer/{customerId}` | REVIEW_REQUEST |
| GET | `/api/service-requests/status/{status}` | REVIEW_REQUEST |
| GET | `/api/service-requests/{id}` | REVIEW_REQUEST |
| PUT | `/api/service-requests/{id}/review` | REVIEW_REQUEST |
| POST | `/api/service-requests/{id}/convert` | CONVERT_REQUEST |
| PUT | `/api/service-requests/{id}/close` | CLOSE_REQUEST |
| PUT | `/api/service-requests/{id}/cancel` | CANCEL_REQUEST |

Request body:

```json
{
  "siteId": 1,
  "title": "AC not working",
  "description": "Cooling issue in first floor"
}
```

Service request statuses:

```text
OPEN
IN_REVIEW
CONVERTED_TO_WORK_ORDER
CLOSED
CANCELLED
```

## 13. Work Order APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| POST | `/api/workorders` | CREATE_WO |
| GET | `/api/workorders` | VIEW_WO |
| GET | `/api/workorders/{id}` | VIEW_WO |
| PUT | `/api/workorders/{id}` | UPDATE_WO |
| DELETE | `/api/workorders/{id}` | DELETE_WO |
| GET | `/api/workorders/site/{siteId}` | VIEW_WO |
| GET | `/api/workorders/technician/{technicianId}` | VIEW_WO |
| GET | `/api/workorders/status/{status}` | VIEW_WO |
| PUT | `/api/workorders/{id}/assign/{technicianId}` | ASSIGN_WO |
| PUT | `/api/workorders/{id}/accept` | START_WORK |
| PUT | `/api/workorders/{id}/start` | START_WORK |
| PUT | `/api/workorders/{id}/hold` | HOLD_WORK |
| PUT | `/api/workorders/{id}/resume` | RESUME_WORK |
| PUT | `/api/workorders/{id}/complete` | COMPLETED_WORK |
| PUT | `/api/workorders/{id}/cancel` | CANCEL_WO |
| PUT | `/api/workorders/{id}/close` | CLOSE_WO |

Request body:

```json
{
  "title": "Repair AC",
  "description": "Inspect and repair AC unit",
  "priority": "HIGH",
  "status": "OPEN",
  "siteId": 1,
  "assignedTechnicianId": 3,
  "scheduledAt": "2026-09-01T10:00:00"
}
```

Work order statuses:

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

## 14. Parts APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| POST | `/api/parts` | ADD_PARTS |
| GET | `/api/parts` | VIEW_PARTS |
| GET | `/api/parts/{id}` | VIEW_PARTS |
| PUT | `/api/parts/{id}` | UPDATE_PARTS |
| PUT | `/api/parts/{id}/stock/{quantity}` | UPDATE_PARTS |
| DELETE | `/api/parts/{id}` | DELETE_PART |

Request body:

```json
{
  "name": "Filter",
  "sku": "FLT-001",
  "quantity": 20,
  "unitPrice": 150.00
}
```

## 15. Work Order Parts APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| POST | `/api/workorder-parts/use` | USE_PARTS |
| GET | `/api/workorder-parts` | VIEW_PARTS |
| GET | `/api/workorder-parts/workorder/{workOrderId}` | VIEW_PARTS |

Request body:

```json
{
  "workOrderId": 1,
  "partId": 2,
  "quantityUsed": 1
}
```

## 16. Time Log APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| POST | `/api/time-logs/start` | ADD_LOG_TIME |
| PUT | `/api/time-logs/{id}/stop` | ADD_LOG_TIME |
| GET | `/api/time-logs` | VIEW_LOG_TIME |
| GET | `/api/time-logs/workorder/{workOrderId}` | VIEW_LOG_TIME |
| GET | `/api/time-logs/technician/{technicianId}` | VIEW_LOG_TIME |

Request body:

```json
{
  "workOrderId": 1,
  "technicianId": 3,
  "notes": "Started diagnosis"
}
```

## 17. Dashboard APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| GET | `/api/dashboard` | VIEW_DASHBOARD |

## 18. SLA APIs

| Method | Endpoint | Permission |
| --- | --- | --- |
| GET | `/api/sla/overdue` | VIEW_REPORTS |
| GET | `/api/sla/overdue/count` | VIEW_REPORTS |

## 19. Common Workflow

1. Create the first manager using `/api/user_auth/setup-manager`.
2. Login as manager.
3. Create dispatcher and technician users.
4. Register or create customer.
5. Create customer site.
6. Customer raises service request.
7. Dispatcher or manager reviews the request.
8. Dispatcher or manager converts the request into a work order.
9. Dispatcher assigns the work order to a technician.
10. Technician accepts and starts the work.
11. Technician logs time and uses parts.
12. Technician completes the work.
13. Dispatcher or manager closes the work order.
14. Manager reviews dashboard and SLA reports.

## 20. Important Notes

1. Keep NeonDB credentials in environment variables.
2. `application.properties` uses environment placeholders.
3. Localhost and live deployment can use the same NeonDB database when the same database variables are configured.
4. Default frontend origin is `http://localhost:5173`.
5. Default backend port is `8081`.
