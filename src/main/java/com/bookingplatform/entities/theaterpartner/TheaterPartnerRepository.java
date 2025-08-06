package com.bookingplatform.entities.theaterpartner;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TheaterPartnerRepository extends R2dbcRepository<TheaterPartner, Long> {
    
    Mono<TheaterPartner> findByEmail(String email);
    
    Mono<Boolean> existsByEmail(String email);
    
    @Query("SELECT * FROM theater_partners WHERE is_active = :isActive ORDER BY company_name")
    Flux<TheaterPartner> findByActiveStatus(boolean isActive);
    
    @Query("SELECT * FROM theater_partners WHERE tax_id = :taxId")
    Mono<TheaterPartner> findByTaxId(String taxId);
}
