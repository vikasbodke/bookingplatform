package com.bookingplatform.entities.movie;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface MovieRepository extends R2dbcRepository<Movie, Long> {
    
    @Query("SELECT * FROM movies WHERE title ILIKE '%' || :title || '%'")
    Flux<Movie> findByTitleContaining(String title);
    
    @Query("SELECT * FROM movies WHERE genre = :genre")
    Flux<Movie> findByGenre(String genre);
    
    @Query("SELECT * FROM movies WHERE release_date >= :fromDate AND release_date <= :toDate")
    Flux<Movie> findByReleaseDateBetween(LocalDate fromDate, LocalDate toDate);
    
    @Query("SELECT * FROM movies WHERE release_date >= CURRENT_DATE")
    Flux<Movie> findUpcomingMovies();
    
    @Query("SELECT * FROM movies WHERE release_date < CURRENT_DATE")
    Flux<Movie> findReleasedMovies();
    
    @Query("""
        SELECT DISTINCT m.* FROM movies m, jsonb_array_elements_text(m.formats) f 
        WHERE f = :format
    """)
    Flux<Movie> findByFormat(String format);
    
    @Query("""
        SELECT DISTINCT m.* FROM movies m, jsonb_array_elements_text(m.languages) l 
        WHERE l = :language
    """)
    Flux<Movie> findByLanguage(String language);
    
    @Query("""
        SELECT m.* FROM movies m 
        WHERE m.movie_id IN (
            SELECT DISTINCT s.movie_id FROM showtimes s 
            WHERE s.theater_id = :theaterId 
            AND s.start_time >= NOW()
        )
    """)
    Flux<Movie> findNowShowingInTheater(Long theaterId);
}
