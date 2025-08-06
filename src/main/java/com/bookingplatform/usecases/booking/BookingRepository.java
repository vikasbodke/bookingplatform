package com.bookingplatform.usecases.booking;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookingRepository extends ReactiveCrudRepository<Booking, Long> {
    
    /**
     * Find all bookings for a specific user
     */
    @Query("SELECT * FROM bookings WHERE user_id = :userId")
    Flux<Booking> findByUserId(Long userId);
    
    /**
     * Find all bookings for a specific showtime
     */
    @Query("SELECT * FROM bookings WHERE showtime_id = :showtimeId")
    Flux<Booking> findByShowtimeId(Long showtimeId);
    
    /**
     * Find all bookings for a specific user with a specific status
     */
    @Query("SELECT * FROM bookings WHERE user_id = :userId AND status = :status")
    Flux<Booking> findByUserIdAndStatus(Long userId, String status);
    
    /**
     * Check if a booking exists for a specific user and showtime
     */
    @Query("SELECT COUNT(*) > 0 FROM bookings WHERE user_id = :userId AND showtime_id = :showtimeId")
    Mono<Boolean> existsByUserIdAndShowtimeId(Long userId, Long showtimeId);
    
    /**
     * Find a booking by ID and user ID
     */
    @Query("SELECT * FROM bookings WHERE booking_id = :bookingId AND user_id = :userId")
    Mono<Booking> findByIdAndUserId(Long bookingId, Long userId);
    
    /**
     * Find a booking by ID
     */
    @Query("SELECT * FROM bookings WHERE booking_id = :bookingId")
    Mono<Booking> findById(Long bookingId);
    
    /**
     * Find all bookings for a specific screen
     */
    @Query("SELECT * FROM bookings WHERE screen_id = :screenId")
    Flux<Booking> findByScreenId(Long screenId);
}
