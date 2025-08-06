package com.bookingplatform.entities.showtime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Repository
public interface ShowtimeRepository extends R2dbcRepository<Showtime, Long> {
    
    @Query("""
        SELECT * FROM showtimes 
        WHERE movie_id = :movieId 
        AND start_time >= :startDate 
        AND start_time < :endDate 
        AND is_active = true
        ORDER BY start_time
    """)
    Flux<Showtime> findByMovieAndDateRange(Long movieId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("""
        SELECT * FROM showtimes 
        WHERE theater_id = :theaterId 
        AND start_time >= :startDate 
        AND start_time < :endDate 
        AND is_active = true
        ORDER BY start_time
    """)
    Flux<Showtime> findByTheaterAndDateRange(Long theaterId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("""
        SELECT * FROM showtimes 
        WHERE screen_id = :screenId 
        AND start_time >= :startTime 
        AND end_time <= :endTime 
        AND is_active = true
    """)
    Flux<Showtime> findConflictingShowtimes(Long screenId, LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT * FROM showtimes WHERE movie_id = :movieId AND is_active = true ORDER BY start_time")
    Flux<Showtime> findByMovieId(Long movieId);
    
    @Query("SELECT * FROM showtimes WHERE theater_id = :theaterId AND is_active = true ORDER BY start_time")
    Flux<Showtime> findByTheaterId(Long theaterId);
    
    @Query("SELECT * FROM showtimes WHERE screen_id = :screenId AND is_active = true ORDER BY start_time")
    Flux<Showtime> findByScreenId(Long screenId);
    
    @Query("""
        SELECT s.* FROM showtimes s
        JOIN movies m ON s.movie_id = m.movie_id
        WHERE m.title ILIKE '%' || :query || '%'
        AND s.is_active = true
        ORDER BY s.start_time
    """)
    Flux<Showtime> searchByMovieTitle(String query);
    
    @Query("""
        SELECT s.* FROM showtimes s
        WHERE s.start_time >= :startTime 
        AND s.start_time < :endTime 
        AND s.is_active = true
        ORDER BY s.start_time
    """)
    Flux<Showtime> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("UPDATE showtimes SET is_active = :isActive WHERE showtime_id = :showtimeId")
    Mono<Integer> updateStatus(Long showtimeId, boolean isActive);
}
