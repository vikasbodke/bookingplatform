package com.bookingplatform.usecases.browsing.repository;

import com.bookingplatform.usecases.browsing.dto.BrowseMovieSearchRequest;
import com.bookingplatform.usecases.browsing.dto.BrowseMovieResponse;

import reactor.core.publisher.Flux;

public interface BrowseMovieRepository {
    Flux<BrowseMovieResponse> findMoviesByCityAndFilters(BrowseMovieSearchRequest searchRequest);
}
