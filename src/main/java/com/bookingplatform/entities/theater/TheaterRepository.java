package com.bookingplatform.entities.theater;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TheaterRepository extends R2dbcRepository<Theater, Long> {
    
    @Query("SELECT * FROM theaters WHERE city_id = :cityId AND is_active = true")
    Flux<Theater> findByCityId(Long cityId);
    
    @Query("SELECT * FROM theaters WHERE partner_id = :partnerId")
    Flux<Theater> findByPartnerId(Long partnerId);
    
    @Query("SELECT * FROM theaters WHERE name ILIKE '%' || :name || '%'")
    Flux<Theater> findByNameContaining(String name);
    
    @Query("SELECT * FROM theaters WHERE is_active = :isActive")
    Flux<Theater> findByActiveStatus(boolean isActive);
    
    @Query("SELECT * FROM theaters WHERE city_id = :cityId AND is_active = true AND name ILIKE '%' || :name || '%'")
    Flux<Theater> findByCityAndName(Long cityId, String name);
    
    @Query("UPDATE theaters SET is_active = :isActive WHERE theater_id = :theaterId")
    Mono<Integer> updateStatus(Long theaterId, boolean isActive);
}
