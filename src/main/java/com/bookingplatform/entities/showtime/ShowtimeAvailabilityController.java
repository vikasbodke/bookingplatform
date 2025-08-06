package com.bookingplatform.entities.showtime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api//v1/showtimes/{showtimeId}/availability")
public class ShowtimeAvailabilityController {

    private final ShowtimeAvailabilityService availabilityService;
    private final ShowtimeService showtimeService;

    public ShowtimeAvailabilityController(
            ShowtimeAvailabilityService availabilityService,
            ShowtimeService showtimeService) {
        this.availabilityService = availabilityService;
        this.showtimeService = showtimeService;
    }

    @GetMapping
    public Mono<ResponseEntity<ShowtimeAvailability>> getAvailability(
            @PathVariable Long showtimeId) {
        return availabilityService.getAvailability(showtimeId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/reserve")
    public Mono<ResponseEntity<Boolean>> reserveSeats(
            @PathVariable Long showtimeId,
            @RequestParam int seats) {
        return availabilityService.hasAvailableSeats(showtimeId, seats)
                .flatMap(hasSeats -> {
                    if (!hasSeats) {
                        return Mono.just(ResponseEntity.badRequest().body(false));
                    }
                    return availabilityService.reserveSeats(showtimeId, seats)
                            .map(ResponseEntity::ok);
                });
    }

    @PostMapping("/release")
    public Mono<ResponseEntity<Boolean>> releaseSeats(
            @PathVariable Long showtimeId,
            @RequestParam int seats) {
        return availabilityService.releaseSeats(showtimeId, seats)
                .map(updated -> updated ?
                        ResponseEntity.ok(true) :
                        ResponseEntity.badRequest().body(false));
    }

    @GetMapping("/status")
    public Mono<ResponseEntity<Boolean>> checkAvailability(
            @PathVariable Long showtimeId,
            @RequestParam(required = false, defaultValue = "1") int seats) {
        return availabilityService.hasAvailableSeats(showtimeId, seats)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/initialize")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ShowtimeAvailability> initialize(
            @PathVariable Long showtimeId,
            @RequestParam int totalSeats) {
        return showtimeService.getShowtimeById(showtimeId)
                .flatMap(showtime -> availabilityService.initializeAvailability(showtime, totalSeats));
    }
}
