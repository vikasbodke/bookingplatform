package com.bookingplatform.entities.showtime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Showtime> createShowtime(@RequestBody Showtime showtime) {
        return showtimeService.createShowtime(showtime);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Showtime>> getShowtimeById(@PathVariable Long id) {
        return showtimeService.getShowtimeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Showtime> getShowtimes(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long theaterId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        if (movieId != null) {
            return showtimeService.getShowtimesByMovie(movieId);
        } else if (theaterId != null) {
            return showtimeService.getShowtimesByTheater(theaterId);
        } else if (start != null && end != null) {
            return showtimeService.getShowtimesByDateRange(start, end);
        } else {
            return showtimeService.getAllShowtimes();
        }
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Showtime>> updateShowtime(
            @PathVariable Long id, @RequestBody Showtime showtime) {
        return showtimeService.updateShowtime(id, showtime)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteShowtime(@PathVariable Long id) {
        return showtimeService.deleteShowtime(id);
    }

    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Object>> updateShowtimeStatus(
            @PathVariable Long id, @RequestParam boolean active) {
        return showtimeService.toggleStatus(id, active)
                .map(updated -> updated ?
                        ResponseEntity.ok().build() :
                        ResponseEntity.notFound().build()
                );
    }
}
