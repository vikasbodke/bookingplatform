package com.bookingplatform.usecases.browsing.service;

import com.bookingplatform.usecases.browsing.dto.ShowtimeInfo;
import com.bookingplatform.usecases.browsing.dto.ShowtimeSearchRequest;
import com.bookingplatform.usecases.browsing.repository.ShowtimeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class BrowseShowtimeService {
    private final ShowtimeRepository showtimeRepository;

    public BrowseShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    public Flux<ShowtimeInfo> findShowtimesByMovieAndFilters(ShowtimeSearchRequest searchRequest) {
        return showtimeRepository.findShowtimesByMovieAndFilters(searchRequest);
    }
}
