package com.bookingplatform.usecases.browsing.repository;

import com.bookingplatform.usecases.browsing.dto.BrowseTheaterInfo;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bookingplatform.usecases.browsing.dto.BrowseMovieResponse;
import com.bookingplatform.usecases.browsing.dto.BrowseMovieSearchRequest;

import reactor.core.publisher.Flux;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BrowseMovieRepositoryImpl implements BrowseMovieRepository {
    private static final Logger logger = LogManager.getLogger(BrowseMovieRepositoryImpl.class);
    
    private final DatabaseClient databaseClient;
    private final ObjectMapper objectMapper;
    
    public BrowseMovieRepositoryImpl(DatabaseClient databaseClient, ObjectMapper objectMapper) {
        this.databaseClient = databaseClient;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    @Override
    public Flux<BrowseMovieResponse> findMoviesByCityAndFilters(BrowseMovieSearchRequest searchRequest) {
        String query = """
            SELECT DISTINCT m.movie_id, m.title, m.genre, 
                   m.languages, m.formats,
                   t.theater_id, t.name as theater_name, t.location
            FROM movies m
            JOIN showtimes s ON m.movie_id = s.movie_id
            JOIN theaters t ON s.theater_id = t.theater_id
            WHERE t.city_id = :cityId
            AND (CAST(:date AS DATE) IS NULL OR EXISTS (
                SELECT 1 FROM showtimes st 
                WHERE st.movie_id = m.movie_id 
                AND st.theater_id = t.theater_id
                AND DATE(st.start_time) = :date
                AND st.start_time >= NOW()
                AND st.is_active = true
            ))
            AND ((:language IS NULL) OR (m.languages @> jsonb_build_array(:language)::jsonb))
            AND ((:theaterId IS NULL) OR (t.theater_id = :theaterId))
            AND ((:format IS NULL) OR (m.formats @> jsonb_build_array(:format)::jsonb))
            AND ((:genre IS NULL) OR (m.genre = :genre))
            ORDER BY m.title
            OFFSET :offset ROWS
            FETCH FIRST :limit ROWS ONLY
            """;

        // Start building the query with required parameters
        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(query)
                .bind("cityId", searchRequest.getCityId())
                .bind("offset", (searchRequest.getPage() - 1) * searchRequest.getLimit())
                .bind("limit", searchRequest.getLimit());

        // Handle optional parameters with null checks
        if (searchRequest.getDate() != null) {
            spec = spec.bind("date", searchRequest.getDate());
        } else {
            spec = spec.bindNull("date", LocalDate.class);
        }

        if (searchRequest.getLanguage() != null) {
            spec = spec.bind("language", searchRequest.getLanguage());
        } else {
            spec = spec.bindNull("language", String.class);
        }

        if (searchRequest.getTheaterId() != null) {
            spec = spec.bind("theaterId", searchRequest.getTheaterId());
        } else {
            spec = spec.bindNull("theaterId", Integer.class);
        }

        if (searchRequest.getFormat() != null) {
            spec = spec.bind("format", searchRequest.getFormat());
        } else {
            spec = spec.bindNull("format", String.class);
        }

        if (searchRequest.getGenre() != null) {
            spec = spec.bind("genre", searchRequest.getGenre());
        } else {
            spec = spec.bindNull("genre", String.class);
        }

        // Execute the query
        return spec.fetch()
                .all()
                .groupBy(row -> (Integer) row.get("movie_id"))
                .flatMap(groupedFlux -> groupedFlux
                    .collectList()
                    .map(rows -> {
                        var firstRow = rows.get(0);
                        // Convert JSONB to List for languages and formats
                        List<String> languages = List.of();
                        List<String> formats = List.of();
                        
                        try {
                            Object languagesObj = firstRow.get("languages");
                            if (languagesObj != null) {
                                if (languagesObj instanceof Json) {
                                    languages = objectMapper.readValue(
                                        ((Json) languagesObj).asString(), 
                                        new TypeReference<List<String>>() {}
                                    );
                                } else if (languagesObj instanceof List) {
                                    languages = (List<String>) languagesObj;
                                }
                            }
                            
                            Object formatsObj = firstRow.get("formats");
                            if (formatsObj != null) {
                                if (formatsObj instanceof Json) {
                                    formats = objectMapper.readValue(
                                        ((Json) formatsObj).asString(), 
                                        new TypeReference<List<String>>() {}
                                    );
                                } else if (formatsObj instanceof List) {
                                    formats = (List<String>) formatsObj;
                                }
                            }
                        } catch (Exception e) {
                            // Log error but continue with empty lists
                            logger.error("Error parsing JSON fields: {}", e.getMessage(), e);
                        }
                            
                        return BrowseMovieResponse.builder()
                            .movieId((Integer) firstRow.get("movie_id"))
                            .title((String) firstRow.get("title"))
                            .genre((String) firstRow.get("genre"))
                            .languages(languages)
                            .formats(formats)
                            .theaters(rows.stream()
                                .map(row -> {
                                    BrowseTheaterInfo browseTheaterInfo = new BrowseTheaterInfo();
                                    browseTheaterInfo.setTheaterId((Integer) row.get("theater_id"));
                                    browseTheaterInfo.setName((String) row.get("theater_name"));
                                    browseTheaterInfo.setLocation((String) row.get("location"));
                                    return browseTheaterInfo;
                                })
                                .toList())
                            .build();
                    }));
    }
}
