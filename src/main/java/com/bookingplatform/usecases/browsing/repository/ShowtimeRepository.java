package com.bookingplatform.usecases.browsing.repository;

import com.bookingplatform.usecases.browsing.dto.ShowtimeInfo;
import com.bookingplatform.usecases.browsing.dto.ShowtimeSearchRequest;
import reactor.core.publisher.Flux;

public interface ShowtimeRepository {
    Flux<ShowtimeInfo> findShowtimesByMovieAndFilters(ShowtimeSearchRequest searchRequest);
}
