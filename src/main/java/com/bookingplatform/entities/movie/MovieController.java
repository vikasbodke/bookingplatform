package com.bookingplatform.entities.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@RestController
@RequestMapping("/api//v1/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Movie> createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Movie>> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Movie> getMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String format) {
        
        return movieService.findMoviesByFilters(title, genre, language, format);
    }

    @GetMapping("/upcoming")
    public Flux<Movie> getUpcomingMovies() {
        return movieService.findUpcomingMovies();
    }

    @GetMapping("/now-showing")
    public Flux<Movie> getNowShowingMovies() {
        return movieService.findNowShowingMovies();
    }

    @GetMapping("/theater/{theaterId}")
    public Flux<Movie> getMoviesByTheater(@PathVariable Long theaterId) {
        return movieService.findNowShowingInTheater(theaterId);
    }

    @GetMapping("/search")
    public Flux<Movie> searchMovies(@RequestParam String query) {
        return movieService.searchMoviesByTitle(query);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Movie>> updateMovie(
            @PathVariable Long id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovie(@PathVariable Long id) {
        return movieService.deleteMovie(id);
    }

    @GetMapping("/release-date")
    public Flux<Movie> getMoviesByReleaseDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return movieService.findMoviesByReleaseDateRange(start, end);
    }
}
