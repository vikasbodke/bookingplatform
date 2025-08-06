package com.bookingplatform.usecases.browsing.service;

import com.bookingplatform.usecases.browsing.dto.BrowseMovieResponse;
import com.bookingplatform.usecases.browsing.dto.BrowseMovieSearchRequest;
import com.bookingplatform.usecases.browsing.repository.BrowseMovieRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class BrowseMovieService {
    private final BrowseMovieRepository browseMovieRepository;

    public BrowseMovieService(BrowseMovieRepository browseMovieRepository) {
        this.browseMovieRepository = browseMovieRepository;
    }

    public Flux<BrowseMovieResponse> findMoviesByCityAndFilters(BrowseMovieSearchRequest searchRequest) {
        return browseMovieRepository.findMoviesByCityAndFilters(searchRequest);
    }
}
