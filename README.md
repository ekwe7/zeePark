# ZeePark — Backend API

A Spring Boot REST API for a smart parking management system. Handles user authentication, vehicle registration, parking session lifecycle, ticket generation, and payment processing.

---

## Tech Stack

- **Java 25**
- **Spring Boot 4.0.5**
- **MongoDB** (Spring Data MongoDB)
- **Gradle**
- **Docker**

---

## Prerequisites

- Java 25+
- MongoDB running on `localhost:27017`
- Gradle (or use the included `./gradlew`)

---

## Getting Started

### 1. Start MongoDB

```bash
docker-compose up mongodb -d
```

### 2. Run the backend

```bash
./gradlew bootRun
```

Server starts on **http://localhost:8181**

Then create zones, spot categories, and parking spots before customers can use the system.

---

## Environment Variables

| Variable | Description | Default |
|---|---|---|
| `MONGODB_URI` | MongoDB connection string | `mongodb://localhost:27017/zeepark` |
| `PORT` | Server port | `8181` |
| `FLW_PUBLIC_KEY` | Flutterwave public key | — |
| `FLW_SECRET_KEY` | Flutterwave secret key | — |
| `FLW_REDIRECT_URL` | Payment redirect URL | `zeepark://payment/success` |
| `PAYPAL_CLIENT_ID` | PayPal client ID | — |
| `PAYPAL_SECRET` | PayPal secret | — |

---

## Docker

```bash
# Build and run everything (app + MongoDB)
docker-compose up --build
```

---

## API Reference

### Auth

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/login` | No | Login |
| POST | `/api/auth/logout` | Yes | Logout |

### Users

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/users/register` | No | Register user |
| GET | `/api/users` | Yes | Get all users |
| GET | `/api/users/{id}` | Yes | Get user by ID |
| DELETE | `/api/users/{id}` | Yes | Delete user |

### Vehicles

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/vehicles` | Yes | Register vehicle |
| GET | `/api/vehicles/my/{customerId}` | Yes | Get customer's vehicles |
| GET | `/api/vehicles/{numberPlate}` | Yes | Get vehicle by plate |
| DELETE | `/api/vehicles/{numberPlate}` | Yes | Delete vehicle |

### Parking

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/parking/start` | Yes | Start parking session |
| POST | `/api/parking/end` | Yes | End parking session |
| GET | `/api/parking/active/{vehicleId}` | Yes | Get active session for vehicle |
| GET | `/api/parking/spots` | Yes | Get all available spots |

### Payments

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/payments` | Yes | Initiate payment (returns checkout URL) |
| GET | `/api/payments/verify` | Yes | Verify payment after redirect |
| GET | `/api/payments/my/{customerId}` | Yes | Get customer's payment history |
| GET | `/api/payments/session/{sessionId}` | Yes | Get payment by session |
| GET | `/api/payments` | Yes | Get all payments (admin) |

### Tickets

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/tickets/search` | Yes | Find ticket by session ID |
| GET | `/api/tickets/my/{customerId}` | Yes | Get customer's tickets |
| GET | `/api/tickets` | Yes | Get all tickets (admin) |

### Admin

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/admin/zones` | Yes | Create parking zone |
| GET | `/api/admin/zones` | Yes | List all zones |
| DELETE | `/api/admin/zones/{zoneId}` | Yes | Delete zone |
| POST | `/api/admin/spot-categories` | Yes | Create spot category |
| GET | `/api/admin/spot-categories` | Yes | List spot categories |
| POST | `/api/admin/spots` | Yes | Create parking spot |
| GET | `/api/admin/spots` | Yes | List all spots |
| POST | `/api/admin/spots/{spotId}/free` | Yes | Force-free a spot |
| GET | `/api/admin/reports/revenue` | Yes | Revenue report (date range) |

---

## Authentication

All protected endpoints require a Bearer token in the `Authorization` header:

```
Authorization: Bearer <token>
```

Tokens are obtained from `POST /api/auth/login` and stored in MongoDB (`user_sessions` collection). Sessions expire after **2 minutes of inactivity** and are automatically cleaned up by MongoDB's TTL index.

---

## Data Models

### User Roles
- `CUSTOMER` — can register vehicles, start/end sessions, make payments
- `ADMIN` — manages zones, spots, categories, views revenue reports
- `STAFF` — operational access (login via admin panel)

### Spot Category Types
`NORMAL` | `VIP` | `STAFF` | `EV` | `EXHIBITION`

### Zone Levels
`L1` | `L2` | `BASEMENT`

### Session Status
`ACTIVE` | `COMPLETED`

### Payment Status
`PENDING` | `COMPLETED` | `FAILED`

---

## Payment Flow

1. User ends parking session
2. App calls `POST /api/payments` with session ID and payment method
3. Backend calculates amount based on vehicle type and duration
4. Backend calls Flutterwave API → returns hosted checkout URL
5. App opens checkout URL in WebView
6. User completes payment on Flutterwave's page
7. Flutterwave redirects to `zeepark://payment/success?transaction_id=...&tx_ref=...`
8. App calls `GET /api/payments/verify` to confirm with Flutterwave
9. Payment marked as `COMPLETED` in database

---

## Event-Driven Architecture

| Event | Trigger | Listener | Action |
|---|---|---|---|
| `ParkingSessionStartedEvent` | Session starts | `TicketGenerationListener` | Creates ticket |
| `ParkingSessionEndedEvent` | Session ends | `SpotReleaseListener` | Logs spot release |
| `PaymentCompletedEvent` | Payment confirmed | `PaymentNotificationListener` | Logs notification |
