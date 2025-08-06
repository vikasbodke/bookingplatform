# Booking Platform - Requirements

## Business Goals

1. **Theater Partners (B2B) Onboarding**
   - Enable seamless onboarding of theater partners
   - Provide tools for managing showtimes and seat inventory

2. **User Experience (B2C)**
   - Browse movies across different cities, languages, and genres
   - Book tickets in advance with a seamless experience

## Functional Requirements

### User Facing Features

1. **Browse Theaters and Showtimes** (*implemented*)
   - View available movies in the user's city, for today or a future date
   - See show timings for a selected date, across theaters
   - Filter by language and format (2D, 3D, IMAX) & genres

2. **Booking Features**
   - Select preferred seats from available options (*implemented*)
   - Apply promotional offers and discounts
   - Receive booking confirmation(*implemented*)

3. **Special Offers**
   - 50% discount on the third ticket
   - 20% discount for afternoon shows

### Theater Partner Features

1. **Theater Management**
   - Create, update, and delete Theater/Screen information (*implemented*)
   - Manage Theater timings and pricing 
   - Handle bulk operations for multiple shows

2. **Seat layout**
   - Allocate seat layout for shows
   - Update seat availability
   - Manage different seat categories and pricing

## Non-Functional Requirements

### Performance
- Page load time under 2 seconds
- Support for 1000+ concurrent users
- Handle peak loads during ticket sales, especially on thursdays to sundays

### Security
- Secure handling of payment information
- Role-based access control
- Protection against common web vulnerabilities

### Availability
- 99.9% uptime for booking services
- Graceful degradation during peak loads

### Scalability
- Horizontal scalability for user-facing services
- Database replication for high availability

### Reporting requirements
- Theater Partners
  1. Booking analytics
  2. Revenue reports
  3. Showtime availability reports
  4. Seat availability reports
- User Reports



