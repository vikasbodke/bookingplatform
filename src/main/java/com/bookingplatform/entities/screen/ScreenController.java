package com.bookingplatform.entities.screen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/screens")
public class ScreenController {

    private final ScreenService screenService;

    @Autowired
    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Screen> createScreen(@RequestBody Screen screen) {
        return screenService.createScreen(screen);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Screen>> getScreenById(@PathVariable Long id) {
        return screenService.getScreenById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Screen> getAllScreens(
            @RequestParam(required = false) Long theaterId,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) Boolean active) {
        
        if (theaterId != null && format != null) {
            return screenService.findScreensByTheaterAndFormat(theaterId, format);
        } else if (theaterId != null) {
            return screenService.findScreensByTheater(theaterId);
        } else if (format != null) {
            return screenService.findScreensByFormat(format);
        } else if (active != null) {
            return screenService.findActiveScreens(active);
        } else {
            return screenService.getAllScreens();
        }
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Screen>> updateScreen(
            @PathVariable Long id, @RequestBody Screen screen) {
        return screenService.updateScreen(id, screen)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteScreen(@PathVariable Long id) {
        return screenService.deleteScreen(id);
    }

    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Object>> updateScreenStatus(
            @PathVariable Long id, @RequestParam boolean active) {
        return screenService.toggleScreenStatus(id, active)
                .map(updated -> updated ? 
                        ResponseEntity.ok().build() : 
                        ResponseEntity.notFound().build()
                );
    }

    @GetMapping("/theater/{theaterId}/number/{screenNumber}")
    public Mono<ResponseEntity<Screen>> getScreenByTheaterAndNumber(
            @PathVariable Long theaterId, 
            @PathVariable Integer screenNumber) {
        return screenService.findScreenByTheaterAndNumber(theaterId, screenNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
