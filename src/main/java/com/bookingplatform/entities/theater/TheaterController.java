package com.bookingplatform.entities.theater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Theater> createTheater(@RequestBody Theater theater) {
        return theaterService.createTheater(theater);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Theater>> getTheaterById(@PathVariable Long id) {
        return theaterService.getTheaterById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Theater> getAllTheaters() {
        return theaterService.getAllTheaters();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Theater>> updateTheater(
            @PathVariable Long id, @RequestBody Theater theater) {
        return theaterService.updateTheater(id, theater)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTheater(@PathVariable Long id) {
        return theaterService.deleteTheater(id);
    }

    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Object>> updateTheaterStatus(
            @PathVariable Long id, @RequestParam boolean active) {
        return theaterService.toggleTheaterStatus(id, active)
                .map(updated -> updated ? 
                        ResponseEntity.ok().build() : 
                        ResponseEntity.notFound().build()
                );
    }

    @GetMapping("/city/{cityId}")
    public Flux<Theater> getTheatersByCity(@PathVariable Long cityId) {
        return theaterService.findTheatersByCity(cityId);
    }

    @GetMapping("/partner/{partnerId}")
    public Flux<Theater> getTheatersByPartner(@PathVariable Long partnerId) {
        return theaterService.findTheatersByPartner(partnerId);
    }

    @GetMapping("/search")
    public Flux<Theater> searchTheaters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) Boolean active) {
        
        if (cityId != null && name != null) {
            return theaterService.findTheatersByCityAndName(cityId, name);
        } else if (name != null) {
            return theaterService.searchTheaters(name);
        } else if (active != null) {
            return theaterService.findActiveTheaters(active);
        } else {
            return theaterService.getAllTheaters();
        }
    }
}
