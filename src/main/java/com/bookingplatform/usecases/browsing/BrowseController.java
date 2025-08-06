package com.bookingplatform.usecases.browsing;

import java.time.LocalDate;

import com.bookingplatform.error.ErrorMessages;
import com.bookingplatform.usecases.browsing.dto.*;
import com.bookingplatform.usecases.browsing.service.BrowseShowtimeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookingplatform.dto.ApiResponse;
import com.bookingplatform.usecases.browsing.dto.BrowseMovieSearchRequest;
import com.bookingplatform.usecases.browsing.service.BrowseMovieService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/browse/")
public class BrowseController {

    private static final Logger LOG = LogManager.getLogger(BrowseController.class);

    private final BrowseMovieService browseMovieService;
    private final BrowseShowtimeService browseShowtimeService;

    public BrowseController(BrowseMovieService browseMovieService, BrowseShowtimeService browseShowtimeService) {
        this.browseMovieService = browseMovieService;
        this.browseShowtimeService = browseShowtimeService;
    }
    @Cacheable(value = "moviesCache", key = "#cityId + '-' + #language + '-' + #theater + '-' + #date + '-' + #format + '-' + #genre + '-' + #page + '-' + #limit")
    @GetMapping("/movies")
    public Mono<ResponseEntity<ApiResponse<MovieSearchResponse>>> getMovies(
            @RequestParam("city") Integer cityId,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer theater,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {

        LOG.info("Received request to get movies for cityId: {}, language: {}, theaterId: {}, date: {}, format: {}, genre: {}, page: {}, limit: {}", cityId, language, theater, date, format, genre, page, limit);
        // Validate limit
        if (limit > 50) {
            limit = 50;
        }

        // Parse and validate date
        LocalDate parsedDate = null;
        if (date != null) {
            try {
                parsedDate = LocalDate.parse(date);
                if (parsedDate.isBefore(LocalDate.now())) {
                    return ErrorMessages.MONO_INVALID_DATE;
                }
            } catch (Exception e) {
                return ErrorMessages.MONO_INVALID_DATE_FORMAT;
            }
        }

        // Build search request
        BrowseMovieSearchRequest searchRequest = BrowseMovieSearchRequest.builder()
                .cityId(cityId)
                .language(language)
                .theaterId(theater)
                .date(parsedDate)
                .format(format)
                .genre(genre)
                .page(page)
                .limit(limit)
                .build();

        LocalDate finalParsedDate = parsedDate;
        return browseMovieService.findMoviesByCityAndFilters(searchRequest)
                .collectList()
                .map(movies -> {
                    if (movies.isEmpty()) {
                        return ErrorMessages.NO_MOVIES_FOUND;
                    }
                    
                    MovieSearchResponse response = MovieSearchResponse.builder()
                            .cityId(cityId)
                            .date(finalParsedDate != null ? finalParsedDate.toString() : LocalDate.now().toString())
                            .movies(movies)
                            .build();
                            
                    return ResponseEntity.ok(ApiResponse.success(response));
                });
    }

    @GetMapping("/showtimes")
    public Mono<ResponseEntity<ApiResponse<ShowtimeResponse>>> getShowtimes(
            @RequestParam("movie") Integer movieId,
            @RequestParam("city") Integer cityId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer theater,
            @RequestParam(required = false) String format,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {

        LOG.info("Received request to get showtimes for movieId: {}, cityId: {}, date: {}", movieId, cityId, date);
        
        // Validate limit
        if (limit > 50) {
            limit = 50;
        }

        // Parse and validate date
        LocalDate parsedDate = LocalDate.now(); // Default to today
        if (date != null && !date.isEmpty()) {
            try {
                parsedDate = LocalDate.parse(date);
                if (parsedDate.isBefore(LocalDate.now())) {
                    return ErrorMessages.INVALID_DATE;
                }
            } catch (Exception e) {
                LOG.error("Error parsing date: {}", date, e);
                return ErrorMessages.INVALID_DATE_FORMAT;
            }
        }

        LOG.info("Received request to get showtimes for movieId: {}, cityId: {}, language: {}, theater: {}, date: {}, format: {}", 
                movieId, cityId, language, theater, date, format);

        // Build search request
        ShowtimeSearchRequest searchRequest = ShowtimeSearchRequest.builder()
                .movieId(movieId)
                .cityId(cityId)
                .date(parsedDate)
                .language(language)
                .theaterId(theater)
                .format(format)
                .page(page-1)
                .limit(limit)
                .build();

        LOG.info("Searching showtimes with request: {}", searchRequest);

        return browseShowtimeService.findShowtimesByMovieAndFilters(searchRequest)
                .collectList()
                .flatMap(showtimes -> {
                    if (showtimes.isEmpty()) {
                        LOG.warn("No showtimes found for request: {}", searchRequest);
                        return ErrorMessages.NO_SHOWTIMES_FOUND;
                    }
                    return Mono.just(ResponseEntity.ok(
                            ApiResponse.success(
                                    ShowtimeResponse.builder()
                                            .movieId(movieId)
                                            .title(showtimes.getFirst().getMovieTitle())
                                            .showtimes(showtimes)
                                            .build()
                            )));
                })
                .onErrorResume(e -> {
                    LOG.error("Error finding showtimes", e);
                    return ErrorMessages.INTERNAL_SERVER_ERROR;
                });
    }
}
