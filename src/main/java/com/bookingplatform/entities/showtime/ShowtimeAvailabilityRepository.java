package com.bookingplatform.entities.showtime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ShowtimeAvailabilityRepository extends R2dbcRepository<ShowtimeAvailability, Long> {
    
    @Query("SELECT * FROM showtime_availability WHERE showtime_id = :showtimeId")
    Mono<ShowtimeAvailability> findByShowtimeId(Long showtimeId);
    
    @Modifying
    @Query("""
        UPDATE showtime_availability 
        SET available_seats = available_seats - :seatsToReserve,
            is_fully_booked = (available_seats - :seatsToReserve) <= 0,
            last_updated = CURRENT_TIMESTAMP
        WHERE showtime_id = :showtimeId 
        AND available_seats >= :seatsToReserve
    """)
    Mono<Integer> reserveSeats(Long showtimeId, int seatsToReserve);
    
    @Modifying
    @Query("""
        UPDATE showtime_availability 
        SET available_seats = available_seats + :seatsToRelease,
            is_fully_booked = false,
            last_updated = CURRENT_TIMESTAMP
        WHERE showtime_id = :showtimeId
    """)
    Mono<Integer> releaseSeats(Long showtimeId, int seatsToRelease);
    
    @Query("SELECT available_seats > 0 FROM showtime_availability WHERE showtime_id = :showtimeId")
    Mono<Boolean> hasAvailableSeats(Long showtimeId);
    
    @Query("SELECT is_fully_booked FROM showtime_availability WHERE showtime_id = :showtimeId")
    Mono<Boolean> isFullyBooked(Long showtimeId);
}
