package com.bookingplatform.entities.theaterpartner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/partners")
public class TheaterPartnerController {

    private final TheaterPartnerService partnerService;

    public TheaterPartnerController(TheaterPartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public Flux<TheaterPartner> getAllPartners() {
        return partnerService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TheaterPartner>> getPartnerById(@PathVariable Long id) {
        return partnerService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public Flux<TheaterPartner> getActivePartners() {
        return partnerService.findActivePartners();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TheaterPartner> createPartner(@RequestBody TheaterPartner partner) {
        return partnerService.save(partner);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TheaterPartner>> updatePartner(
            @PathVariable Long id, 
            @RequestBody TheaterPartner partner) {
        return partnerService.update(id, partner)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Void>> updatePartnerStatus(
            @PathVariable Long id, 
            @RequestParam boolean isActive) {
        return partnerService.updateStatus(id, isActive)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePartner(@PathVariable Long id) {
        return partnerService.deleteById(id);
    }
}
