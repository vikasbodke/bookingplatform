package com.bookingplatform.entities.screen;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ScreenRepository extends R2dbcRepository<Screen, Long> {
    
    @Query("SELECT * FROM screens WHERE theater_id = :theaterId AND is_active = true")
    Flux<Screen> findByTheaterId(Long theaterId);
    
    @Query("SELECT * FROM screens WHERE theater_id = :theaterId AND screen_number = :screenNumber")
    Mono<Screen> findByTheaterAndScreenNumber(Long theaterId, Integer screenNumber);
    
    @Query("SELECT * FROM screens WHERE format = :format AND is_active = true")
    Flux<Screen> findByFormat(String format);
    
    @Query("SELECT * FROM screens WHERE is_active = :isActive")
    Flux<Screen> findByActiveStatus(boolean isActive);
    
    @Query("UPDATE screens SET is_active = :isActive WHERE screen_id = :screenId")
    Mono<Integer> updateStatus(Long screenId, boolean isActive);
    
    @Query("SELECT * FROM screens WHERE theater_id = :theaterId AND is_active = true AND format = :format")
    Flux<Screen> findByTheaterAndFormat(Long theaterId, String format);
}
