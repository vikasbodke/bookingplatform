package com.bookingplatform.entities.seatbooking;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SeatBookingRepository extends ReactiveCrudRepository<SeatBooking, Long> {
    
    @Query("SELECT * FROM seat_bookings WHERE showtime_id = :showtimeId")
    Flux<SeatBooking> findByShowtimeId(Long showtimeId);
    
    @Query("""
        SELECT * FROM seat_bookings 
        WHERE showtime_id = :showtimeId 
        AND row_label = :rowLabel 
        AND seat_number = :seatNumber
        AND status = 'BOOKED'
    """)
    Mono<SeatBooking> findBookedSeat(Long showtimeId, String rowLabel, Integer seatNumber);
    
    @Query("""
        SELECT * FROM seat_bookings 
        WHERE showtime_id = :showtimeId 
        AND status = :status
    """)
    Flux<SeatBooking> findByShowtimeIdAndStatus(Long showtimeId, String status);
    
    @Query("""
        SELECT COUNT(*) > 0 FROM seat_bookings 
        WHERE showtime_id = :showtimeId 
        AND row_label = :rowLabel 
        AND seat_number = :seatNumber
        AND status = 'BOOKED'
    """)
    Mono<Boolean> isSeatBooked(Long showtimeId, String rowLabel, Integer seatNumber);
    
    /**
     * Check if a booking reference exists
     * @param bookingReference The booking reference to check
     * @return true if the booking reference exists, false otherwise
     */
    @Query("""
        SELECT COUNT(*) > 0 FROM seat_bookings 
        WHERE booking_reference = :bookingReference
    """)
    Mono<Boolean> existsByBookingReference(String bookingReference);
    
    @Query("""
        UPDATE seat_bookings 
        SET status = :newStatus, updated_at = CURRENT_TIMESTAMP
        WHERE seat_booking_id = :seatBookingId
        RETURNING *
    """)
    Mono<SeatBooking> updateStatus(Long seatBookingId, String newStatus);
}
