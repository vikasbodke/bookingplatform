package com.bookingplatform.usecases.browsing.repository;

import com.bookingplatform.usecases.browsing.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ShowtimeRepositoryImpl implements ShowtimeRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ShowtimeRepositoryImpl.class);

    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;

    public ShowtimeRepositoryImpl(DatabaseClient databaseClient, ObjectMapper objectMapper) {
        this.databaseClient = databaseClient;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    @Override
    public Flux<ShowtimeInfo> findShowtimesByMovieAndFilters(ShowtimeSearchRequest searchRequest) {
        LOG.info("Executing showtimes query with parameters: movieId={}, cityId={}, date={}, language={}, theaterId={}, format={}, offset={}, limit={}",
                searchRequest.getMovieId(), searchRequest.getCityId(), 
                searchRequest.getDate() != null ? searchRequest.getDate().toString() : null,
                searchRequest.getLanguage(), searchRequest.getTheaterId(), 
                searchRequest.getFormat(), searchRequest.getOffset(), searchRequest.getLimit());
                
        // Log the current time for debugging
        LOG.info("Current time: {}", LocalDateTime.now());
                        String query = """
            SELECT 
                st.showtime_id, 
                m.movie_id, m.title, m.languages as movie_languages, m.duration, m.formats as movie_formats,
                t.theater_id, t.name as theater_name, t.location as theater_location, 
                c.city_id, c.name as city_name, c.state,
                s.screen_id, s.screen_number, s.format as screen_format, s.capacity as total_seats,
                st.language, st.format as showtime_format, 
                st.start_time, st.end_time,
                COALESCE(sa.available_seats, s.capacity) as available_capacity
            FROM showtimes st
            JOIN movies m ON st.movie_id = m.movie_id
            JOIN theaters t ON st.theater_id = t.theater_id
            JOIN cities c ON t.city_id = c.city_id
            JOIN screens s ON st.screen_id = s.screen_id
            LEFT JOIN showtime_availability sa ON st.showtime_id = sa.showtime_id
            WHERE m.movie_id = :movieId
            AND c.city_id = :cityId
            AND DATE(st.start_time) = :requestDate
            AND st.is_active = true
            AND ((:language IS NULL) OR (st.language = :language))
            AND ((:theaterId IS NULL) OR (t.theater_id = :theaterId))
            AND ((:format IS NULL) OR (st.format = :format))
            ORDER BY t.name, st.start_time
            OFFSET :offset ROWS
            FETCH FIRST :limit ROWS ONLY
            """;


        // Get the requested date or use current date if not provided
        LocalDate requestDate = searchRequest.getDate() != null ? 
                searchRequest.getDate() : LocalDate.now();

        LOG.info("Querying showtimes for date: {}", requestDate);
        
        // We'll use the date directly in the SQL query with DATE() function
        LOG.debug("Executing SQL query: {}", query.replace("\n", " "));

        // Create the base query
        DatabaseClient.GenericExecuteSpec querySpec = databaseClient.sql(query)
                .bind("movieId", searchRequest.getMovieId())
                .bind("cityId", searchRequest.getCityId())
                .bind("requestDate", requestDate)
                .bind("offset", searchRequest.getOffset())
                .bind("limit", searchRequest.getLimit());

        // Handle optional parameters
        if (searchRequest.getLanguage() != null) {
            querySpec = querySpec.bind("language", searchRequest.getLanguage());
            LOG.debug("Binding language parameter: {}", searchRequest.getLanguage());
        } else {
            querySpec = querySpec.bindNull("language", String.class);
            LOG.debug("Binding language parameter as NULL");
        }

        if (searchRequest.getTheaterId() != null) {
            querySpec = querySpec.bind("theaterId", searchRequest.getTheaterId());
            LOG.debug("Binding theaterId parameter: {}", searchRequest.getTheaterId());
        } else {
            querySpec = querySpec.bindNull("theaterId", Integer.class);
            LOG.debug("Binding theaterId parameter as NULL");
        }

        if (searchRequest.getFormat() != null) {
            querySpec = querySpec.bind("format", searchRequest.getFormat());
            LOG.debug("Binding format parameter: {}", searchRequest.getFormat());
        } else {
            querySpec = querySpec.bindNull("format", String.class);
            LOG.debug("Binding format parameter as NULL");
        }

        // Log the final SQL query with parameters
        LOG.info("Final query: {}", query
            .replace(":movieId", String.valueOf(searchRequest.getMovieId()))
            .replace(":cityId", String.valueOf(searchRequest.getCityId()))
            .replace(":requestDate", "'" + requestDate + "'"));
            
        return querySpec.fetch()
                .all()
                .doOnNext(row -> LOG.info("Found showtime row: {}", row))
                .map(row -> {
                    LOG.info("Mapping row to ShowtimeInfo: {}", row);
                    return mapToShowtimeInfo(row);
                })
                .doOnComplete(() -> LOG.info("Completed showtimes query"))
                .doOnError(e -> LOG.error("Error executing showtimes query", e));
    }

    private ShowtimeInfo mapToShowtimeInfo(Map<String, Object> row) {
        try {
            // Parse movie languages from JSONB if needed
            List<String> movieLanguages = List.of();
            Object languagesObj = row.get("movie_languages");
            if (languagesObj != null) {
                if (languagesObj instanceof Json) {
                    movieLanguages = objectMapper.readValue(
                        ((Json) languagesObj).asString(), 
                        new TypeReference<List<String>>() {}
                    );
                } else if (languagesObj instanceof List) {
                    movieLanguages = (List<String>) languagesObj;
                }
            }
            
            // Parse movie formats from JSONB if needed
            List<String> movieFormats = List.of();
            Object formatsObj = row.get("movie_formats");
            if (formatsObj != null) {
                if (formatsObj instanceof Json) {
                    movieFormats = objectMapper.readValue(
                        ((Json) formatsObj).asString(),
                        new TypeReference<List<String>>() {}
                    );
                } else if (formatsObj instanceof List) {
                    movieFormats = (List<String>) formatsObj;
                }
            }

            // Create and populate MovieInfo DTO
            MovieInfo movieInfo = MovieInfo.builder()
                .movieId(((Number) row.get("movie_id")).longValue())
                .title((String) row.get("title"))
                .languages(movieLanguages)
                .format(movieFormats.stream().collect(Collectors.joining(",")))
                .duration((Integer) row.get("duration"))
                .build();

            // Create and populate TheaterInfo DTO
            TheaterInfo theaterInfo = TheaterInfo.builder()
                .theaterId(((Number) row.get("theater_id")).longValue())
                .name((String) row.get("theater_name"))
                .address((String) row.get("theater_location"))
                .city((String) row.get("city_name"))
                .state((String) row.get("state"))
                .pincode((String) row.get("pincode"))
                .latitude(row.get("latitude") != null ? ((Number) row.get("latitude")).doubleValue() : null)
                .longitude(row.get("longitude") != null ? ((Number) row.get("longitude")).doubleValue() : null)
                .build();

            // Create and populate ScreenInfo DTO
            ScreenInfo screenInfo = ScreenInfo.builder()
                .screenId(((Number) row.get("screen_id")).longValue())
                .screenNumber((Integer) row.get("screen_number"))
                .screenType((String) row.get("screen_format"))
                .totalSeats(((Number) row.get("total_seats")).intValue())
                .theaterId(((Number) row.get("theater_id")).longValue())
                .build();

            return ShowtimeInfo.builder()
                    .showtimeId(((Number) row.get("showtime_id")).longValue())
                    .movie(movieInfo)
                    .theater(theaterInfo)
                    .screen(screenInfo)
                    .language((String) row.get("language"))
                    .format((String) row.get("showtime_format"))
                    .startTime(((LocalDateTime) row.get("start_time")))
                    .endTime(((LocalDateTime) row.get("end_time")))
                    .availableCapacity(((Number) row.get("available_capacity")).intValue())
                    .build();
        } catch (Exception e) {
            LOG.error("Error mapping showtime data: {}", e.getMessage(), e);
            throw new RuntimeException("Error mapping showtime data", e);
        }
    }
}
