# Theater Partners API Documentation

## Overview
This documentation describes the API endpoints available for theater partners of the Booking Platform. These endpoints allow theater partners to manage their theaters, screens, showtimes, and seat inventory.

## Base URL
`https://api.bookingplatform.com/v1/`

## Authentication
All endpoints require authentication using a Bearer token.

## Endpoints

### 1. Onboard a New Theater

#### `POST /theaters`

**Description:**  
Allows a theater partner to onboard a new theater with its initial details, including screens and seat layouts.

**Version:**  
`v1`

**Authentication Required:**  
Yes (Bearer token)

#### Request Headers
| Header        | Type   | Required | Description          |
|---------------|--------|----------|----------------------|
| Authorization| string | Yes      | Bearer token         |
| Content-Type | string | Yes      | application/json     |

#### Request Body
```json
{
  "partner_id": 1001,
  "theater_name": "PVR Orion Mall",
  "city_id": 101,
  "screens": [
    {
      "screen_number": 1,
      "capacity": 200,
      "is_3d_supported": true,
      "audio_type": "Dolby Atmos",
      "layout": {
        "rowGroups": [
          {
            "groupId": "550e8400-e29b-41d4-a716-446655440000",
            "priceTier": "Premium",
            "totalSeats": 40,
            "seatType": "Premium",
            "rows": [
              { "rowLabel": "A", "totalSeats": 20 },
              { "rowLabel": "B", "totalSeats": 20 }
            ]
          },
          {
            "priceTier": "Standard",
            "totalSeats": 60,
            "seatType": "Standard",
            "rows": [
              { "rowLabel": "C", "totalSeats": 20 },
              { "rowLabel": "D", "totalSeats": 20 },
              { "rowLabel": "E", "totalSeats": 20 }
            ]
          }
        ]
      }
    }
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
    "theater_id": 301,
    "status": "ONBOARDED"
  }
}
```

##### Error Responses
**Status:** `400 Bad Request`
```json
{
  "status": "error",
  "code": "INVALID_THEATER_DETAILS",
  "message": "Invalid theater details provided.",
  "details": {
    "field": "Specific validation error details"
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

### 2. De-board a Theater

#### `DELETE /theaters/{theaterId}`

**Description:**  
Allows a theater partner to remove an existing theater from the platform.

**Version:**  
`v1`

**Authentication Required:**  
Yes (Bearer token)

#### Path Parameters
| Parameter | Type   | Required | Description          |
|-----------|--------|----------|----------------------|
| theaterId | integer| Yes      | The ID of the theater to remove |

#### Responses

##### Success Response
**Status:** `200 OK`
```json
{
  "status": "success",
  "data": {
    "theater_id": 301,
    "status": "DE-BOARDED"
  }
}
```

##### Error Responses
**Status:** `404 Not Found`
```json
{
  "status": "error",
  "code": "THEATER_NOT_FOUND",
  "message": "The specified theater was not found."
}
```

### 3. Modify Theater Details

#### `PATCH /theaters/{theaterId}`

**Description:**  
Modify theater details, such as adding or disabling screens.

**Version:**  
`v1`

**Authentication Required:**  
Yes (Bearer token)

#### Path Parameters
| Parameter | Type   | Required | Description          |
|-----------|--------|----------|----------------------|
| theaterId | integer| Yes      | The ID of the theater to modify |

#### Request Body
```json
{
  "screens": [
    {
      "screen_number": 3,
      "capacity": 180,
      "status": "ADDED",
      "is_3d_supported": true,
      "audio_type": "Dolby Digital",
      "layout": {
        "rowGroups": []
      }
    },
    {
      "screen_number": 1,
      "status": "DISABLED"
    }
  ]
}
```

#### Responses

##### Success Response
**Status:** `200 OK`
```json
{
  "status": "success",
  "data": {
    "theater_id": 301,
    "status": "UPDATED"
  }
}
```

### 4. Manage Showtimes

#### `POST /showtimes`

**Description:**  
Allows a theater partner to create, update, or delete showtimes for its screens.

**Version:**  
`v1`

**Authentication Required:**  
Yes (Bearer token)

#### Request Body
```json
{
  "theater_id": 301,
  "action": "CREATE",
  "showtimes": [
    {
      "showtime_id": 1001,
      "screen_number": 1,
      "movie_id": 201,
      "start_time": "2025-08-04T15:30:00Z",
      "end_time": "2025-08-04T18:00:00Z"
    }
  ]
}
```

#### Responses

##### Success Response
**Status:** `200 OK`
```json
{
  "status": "success",
  "data": {
    "theater_id": 301,
    "status": "SHOWTIMES_UPDATED"
  }
}
```

### 5. Bulk Booking and Cancellation

#### `POST /bookings/bulk`

**Description:**  
Allows a theater partner to perform bulk booking or cancellation for shows, specifying exact seats.

**Version:**  
`v1`

**Authentication Required:**  
Yes (Bearer token)

#### Request Body
```json
{
  "theater_id": 301,
  "action": "BOOK",
  "bookings": [
    {
      "showtime_id": 1001,
      "seats": [
        { "row": "A", "seat_numbers": [1, 2, 3] },
        { "row": "B", "seat_numbers": [4, 5] }
      ]
    }
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
    "theater_id": 301,
    "status": "BULK_BOOKING_COMPLETED"
  }
}
```

### 6. Manage Seat Inventory

#### `POST /seats/inventory`

**Description:**  
Allows a theater partner to allocate or update seat inventory for a specific showtime.

**Version:**  
`v1`

**Authentication Required:**  
Yes (Bearer token)

#### Request Body
```json
{
  "theater_id": 301,
  "showtime_id": 1001,
  "layout": {
    "rowGroups": [
      {
        "groupId": "550e8400-e29b-41d4-a716-446655440000",
        "priceTier": "Premium",
        "totalSeats": 40,
        "seatType": "Premium",
        "rows": [
          { "rowLabel": "A", "seatNumbers": [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20] },
          { "rowLabel": "B", "seatNumbers": [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20] }
        ]
      }
    ]
  }
}
```

#### Responses

##### Success Response
**Status:** `200 OK`
```json
{
  "status": "success",
  "data": {
    "theater_id": 301,
    "status": "SEAT_INVENTORY_UPDATED"
  }
}
```

## Error Handling

### Standard Error Response Format
All error responses follow this format:

```json
{
  "status": "error",
  "code": "ERROR_CODE",
  "message": "Human-readable error message",
  "details": {
    // Optional additional error details
  }
}
```

### Common Error Codes
| Code                    | HTTP Status | Description                           |
|-------------------------|-------------|---------------------------------------|
| INVALID_THEATER_DETAILS | 400         | Invalid theater details provided      |
| INVALID_SHOWTIME        | 400         | Invalid showtime data                 |
| INVALID_SEAT_REQUEST    | 400         | Invalid seat selection or layout      |
| UNAUTHORIZED           | 401         | Authentication required              |
| FORBIDDEN              | 403         | Insufficient permissions             |
| THEATER_NOT_FOUND      | 404         | Specified theater not found          |
| SHOWTIME_NOT_FOUND     | 404         | Specified showtime not found         |
| INTERNAL_ERROR         | 500         | Internal server error                |

## Rate Limiting
- All endpoints: 1000 requests per minute per authenticated partner
- Headers are included in all responses:
  - `X-RateLimit-Limit`: Request limit per time window
  - `X-RateLimit-Remaining`: Remaining requests in current window
  - `X-RateLimit-Reset`: Time when the rate limit resets (UTC epoch seconds)
