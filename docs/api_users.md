# Users API Documentation

## Overview
This documentation describes the API endpoints available for users of the Booking Platform. These endpoints allow users to browse movies, check showtimes, and make bookings.

## Base URL
`https://api.bookingplatform.com/v1/`

## Authentication
Not required for browsing movies and showtimes. Authentication is required for booking endpoints.

## Endpoints

### 1. Get Movies in a City

#### `GET /api/v1/browse/movies`

**Description:**  
Returns a list of movies available in the specified city on a particular date. Optionally filter by language, theater, format, and genre.

** Caching: **
This endpoint is cached for 1 hour.
**Version:**  
`v1`

**Authentication Required:**  
No

#### Parameters

##### Query Parameters
| Parameter | Type    | Required | Description |
|-----------|---------|----------|-------------|
| city      | integer | Yes      | The city ID to fetch movies for |
| language  | string  | No       | Language filter (e.g., "English", "Hindi") |
| theater   | integer | No       | Filter by theater ID |
| date      | string (YYYY-MM-DD) | No | Date to filter movies (defaults to current date). Must be today or a future date. |
| format    | string  | No       | Filter by format (e.g., "2D", "3D", "IMAX") |
| genre     | string  | No       | Filter by genre (e.g., "Action", "Drama") |
| page      | integer | No       | Page number (default: 1) |
| limit     | integer | No       | Number of items per page (default: 20, max: 50) |

#### Responses

##### Success Response
**Status:** `200 OK`
```json
{
  "status": "success",
  "data": {
    "city_id": 101,
    "date": "2025-08-04",
    "movies": [
      {
        "movie_id": 201,
        "title": "Inception",
        "genre": "Sci-Fi",
        "languages": ["English", "Hindi"],
        "formats": ["2D", "IMAX"],
        "theaters": [
          {
            "theater_id": 301,
            "name": "PVR Orion Mall",
            "location": "Rajajinagar"
          }
        ]
      }
    ]
  }
}
```

##### Error Responses
**Status:** `400 Bad Request`
```json
{
  "status": "error",
  "code": "INVALID_DATE",
  "message": "Invalid date. Date must be today or in the future."
}
```

**Status:** `404 Not Found`
```json
{
  "status": "error",
  "code": "NO_MOVIES_FOUND",
  "message": "No movies found for the given city and date."
}
```

---

### 2. Get Showtimes for a Movie

#### `GET /api/v1/browse/showtimes`

**Description:**  
Returns the list of showtimes for a specific movie in a given city on a specified date. 
Optionally filter by language, theater, and format.


**Version:**  
`v1`

**Authentication Required:**  
No

#### Parameters

##### Query Parameters
| Parameter | Type    | Required | Description |
|-----------|---------|----------|-------------|
| movie     | integer | Yes      | Movie ID |
| city      | integer | Yes      | City ID to filter showtimes |
| language  | string  | No       | Language filter (e.g., "English", "Hindi") |
| theater   | integer | No       | Filter by theater ID |
| date      | string (YYYY-MM-DD) | No | Date to filter showtimes (defaults to current date). Must be today or a future date. |
| format    | string  | No       | Filter by format (e.g., "2D", "3D", "IMAX") |
| page      | integer | No       | Page number (default: 1) |
| limit     | integer | No       | Number of items per page (default: 20, max: 50) |

#### Responses

##### Success Response
**Status:** `200 OK`
```json
{
  "status": "success",
  "data": {
    "movie_id": 201,
    "title": "Inception",
    "languages": ["English", "Hindi"],
    "date": "2025-08-04",
    "showtimes": [
      {
        "theater_id": 301,
        "theater_name": "PVR Orion Mall",
        "screen_id": 1,
        "language": "English",
        "format": "IMAX",
        "start_time": "2025-08-04T15:30:00Z",
        "end_time": "2025-08-04T18:00:00Z",
        "available_capacity": 120
      }
    ]
  }
}
```

##### Error Responses
**Status:** `400 Bad Request`
```json
{
  "status": "error",
  "code": "INVALID_DATE",
  "message": "Invalid date. Date must be today or in the future."
}
```

**Status:** `404 Not Found`
```json
{
  "status": "error",
  "code": "NO_SHOWTIMES_FOUND",
  "message": "No showtimes found for the given movie, city, and date."
}
```

---

### 3. Initiate Booking

#### `POST /bookings`

**Description:**  
Initiates a new booking for the given showtime and seats. Seats are specified as per the theater layout using row and seat labels.

**Version:**  
`v1`

**Authentication Required:**  
Yes (via Bearer token)

#### Request Headers
| Header        | Type   | Required | Description          |
|---------------|--------|----------|----------------------|
| Authorization| string | Yes      | Bearer token         |
| Content-Type | string | Yes      | application/json     |

#### Request Body
```json
{
  "showtime_id": 701,
  "selectedSeats": [
    { "row": "A", "seats": [1, 2] },
    { "row": "B", "seats": [3, 4] }
  ]
}
```

#### Responses

##### Success Response
**Status:** `201 Created`
```json
{
  "status": "success",
  "data": {
    "bookingId": 4,
    "showtimeId": 19,
    "screenId": null,
    "bookingTime": "2025-08-05T23:16:54.5216032",
    "totalAmount": 20.0,
    "status": "PENDING"
  },
  "error": null
}
```

##### Error Responses
**Status:** `400 Bad Request`
```json
{
  "status": "error",
  "code": "UNAVAILABLE_SEATS",
  "message": "Requested seats are no longer available or the date is invalid.",
  "details": {
    "unavailable_seats": ["A1", "B3"]
  }
}
```

**Status:** `401 Unauthorized`
```json
{
  "status": "error",
  "code": "UNAUTHORIZED",
  "message": "Authentication required"
}
```

**Status:** `404 Not Found`
```json
{
  "status": "error",
  "code": "SHOWTIME_NOT_FOUND",
  "message": "The requested showtime does not exist."
}
```

---

### 4. Get Seat Availability for a Showtime

#### `GET /showtimes/{showtime_id}/seats`

**Description:**  
Returns the seat layout and current availability for a specific showtime, including seat categories and status (available/booked/held).

**Version:**  
`v1`

**Authentication Required:**  
No

#### Path Parameters
| Parameter    | Type    | Required | Description         |
|--------------|---------|----------|---------------------|
| showtime_id  | integer | Yes      | ID of the showtime to check |

#### Responses

##### Success Response
**Status:** `200 OK`
```json
{
  "status": "success",
  "data": {
    "showtime_id": 701,
    "screen_id": 11,
    "layout": {
      "rowGroups": [
        {
          "groupId": "550e8400-e29b-41d4-a716-446655440000",
          "priceTier": "Premium",
          "totalSeats": 40,
          "seatType": "Premium",
          "rows": [
            {
              "rowLabel": "A",
              "totalSeats": 20,
              "seats": [
                { "number": 1, "status": "available" },
                { "number": 2, "status": "booked" },
                { "number": 3, "status": "unavailable" }
              ]
            },
            {
              "rowLabel": "B",
              "totalSeats": 20,
              "seats": [
                { "number": 1, "status": "unavailable" },
                { "number": 2, "status": "available" },
                { "number": 3, "status": "booked" }
              ]
            }
          ]
        },
        {
          "groupId": "660e8400-e29b-41d4-a716-446655440000",
          "priceTier": "Standard",
          "totalSeats": 60,
          "seatType": "Standard",
          "rows": [
            {
              "rowLabel": "C",
              "totalSeats": 30,
              "seats": [
                { "number": 1, "status": "available" },
                { "number": 2, "status": "unavailable" },
                { "number": 3, "status": "booked" }
              ]
            },
            {
              "rowLabel": "D",
              "totalSeats": 30,
              "seats": [
                { "number": 1, "status": "available" },
                { "number": 2, "status": "unavailable" },
                { "number": 3, "status": "available" }
              ]
            }
          ]
        }
      ]
    },
    "last_updated": "2025-08-04T16:15:00Z"
  }
}
```

##### Error Responses
**Status:** `404 Not Found`
```json
{
  "status": "error",
  "code": "SHOWTIME_NOT_FOUND",
  "message": "No showtime found with the provided ID."
}
```

**Status:** `500 Internal Server Error`
```json
{
  "status": "error",
  "code": "INTERNAL_ERROR",
  "message": "An unexpected error occurred."
}
```

---

### 5. Cancel Booking

#### `DELETE /bookings/{booking_id}`

**Description:**  
Cancels an existing booking. The response includes the refund amount, calculated according to refund rules (e.g., time left before showtime).

**Version:**  
`v1`

**Authentication Required:**  
Yes (via Bearer token)

#### Request Headers
| Header        | Type   | Required | Description          |
|---------------|--------|----------|----------------------|
| Authorization | string | Yes      | Bearer token         |

#### Path Parameters
| Parameter    | Type    | Required | Description         |
|--------------|---------|----------|---------------------|
| booking_id   | integer | Yes      | ID of the booking to cancel |

#### Responses

##### Success Response
**Status:** `200 OK`
```json
{
  "status": "success",
  "data": {
    "booking_id": 801,
    "refund_amount": 800.00,
    "currency": "INR",
    "message": "Booking cancelled. Refund will be processed as per policy."
  }
}
```

##### Error Responses
**Status:** `404 Not Found`
```json
{
  "status": "error",
  "code": "BOOKING_NOT_FOUND",
  "message": "No booking found with the provided ID."
}
```

**Status:** `400 Bad Request`
```json
{
  "status": "error",
  "code": "CANCELLATION_NOT_ALLOWED",
  "message": "Booking cannot be cancelled as per refund policy."
}
```

---

### 6. Get User Bookings

#### `GET /bookings`

**Description:**  
Retrieves a paginated list of bookings for the authenticated user.

**Version:**  
`v1`

**Authentication Required:**  
Yes (via Bearer token)

#### Query Parameters

| Parameter | Type    | Required | Description                                      |
|-----------|---------|----------|--------------------------------------------------|
| page      | integer | No       | Page number (default: 1)                         |
| limit     | integer | No       | Number of items per page (default: 10, max: 50) |
| status    | string  | No       | Filter by booking status (e.g., 'confirmed', 'cancelled') |
| from_date | string  | No       | Filter bookings from this date (YYYY-MM-DD)      |
| to_date   | string  | No       | Filter bookings up to this date (YYYY-MM-DD)     |

#### Headers

```
Authorization: Bearer <JWT_TOKEN>
```

#### Response

##### Success Response
**Status:** `200 OK`
```json
{
  "success": true,
  "data": {
    "bookings": [
      {
        "id": "b123e4567-e89b-12d3-a456-426614174000",
        "movie_title": "Inception",
        "theater_name": "PVR Cinemas",
        "screen_name": "Screen 1",
        "showtime": "2025-08-15T18:30:00+05:30",
        "booking_date": "2025-08-04T14:30:00+05:30",
        "status": "confirmed",
        "total_amount": 750.00,
        "currency": "INR",
        "seats": ["A1", "A2", "A3"],
        "refund_amount": null,
        "cancellation_time": null
      }
    ],
    "pagination": {
      "current_page": 1,
      "total_pages": 5,
      "total_items": 42,
      "items_per_page": 10
    }
  },
  "message": "Bookings retrieved successfully"
}
```

##### Error Responses
**Status:** `401 Unauthorized`
```json
{
  "success": false,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Authentication required"
  }
}
```

### Error Responses

| Status Code | Error Code         | Description                           |
|-------------|--------------------|---------------------------------------|
| 400         | INVALID_DATE_RANGE | Invalid date range provided           |
| 401         | UNAUTHORIZED       | Missing or invalid authentication     |
| 429         | RATE_LIMIT_EXCEEDED| Too many requests                     |

### Example Request

```bash
curl -X GET 'https://api.bookingplatform.com/v1/bookings?status=confirmed&page=1&limit=5' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
```

### Notes

- The endpoint requires authentication via JWT token
- Bookings are returned in reverse chronological order (newest first)
- The response includes pagination metadata for navigating through results
- Filtering by date range is inclusive of both from_date and to_date
- The status filter accepts: 'confirmed', 'cancelled', or 'refunded'

## Error Handling

### Standard Error Response Format
All error responses follow this format:

```json
{
  "status": "error",
  "code": "ERROR_CODE",
  "message": "Human-readable error message",
  "details": {
//     Optional additional error details
  }
}
```

### Common Error Codes
| Code                | HTTP Status | Description                           |
|---------------------|-------------|---------------------------------------|
| INVALID_DATE        | 400         | Invalid or past date provided         |
| UNAVAILABLE_SEATS   | 400         | Requested seats are not available     |
| UNAUTHORIZED        | 401         | Authentication required              |
| SHOWTIME_NOT_FOUND  | 404         | The specified showtime doesn't exist |
| NO_MOVIES_FOUND     | 404         | No movies found for the criteria     |
| NO_SHOWTIMES_FOUND  | 404         | No showtimes found for the criteria  |
| INTERNAL_ERROR      | 500         | Internal server error                |
| BOOKING_NOT_FOUND   | 404         | No booking found with the provided ID |
| CANCELLATION_NOT_ALLOWED | 400 | Booking cannot be cancelled as per refund policy |

## Rate Limiting
- Public endpoints: 100 requests per minute per IP
- Authenticated endpoints: 1000 requests per minute per user
- Headers are included in all responses:
  - `X-RateLimit-Limit`: Request limit per time window
  - `X-RateLimit-Remaining`: Remaining requests in current window
  - `X-RateLimit-Reset`: Time when the rate limit resets (UTC epoch seconds)
