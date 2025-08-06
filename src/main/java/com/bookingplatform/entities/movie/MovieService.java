package com.bookingplatform.entities.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Mono<Movie> createMovie(Movie movie) {
        movie.updateTimestamps();
        return movieRepository.save(movie);
    }

    public Mono<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Flux<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Mono<Movie> updateMovie(Long id, Movie movie) {
        return movieRepository.findById(id)
                .flatMap(existingMovie -> {
                    existingMovie.setTitle(movie.getTitle());
                    existingMovie.setGenre(movie.getGenre());
                    existingMovie.setReleaseDate(movie.getReleaseDate());
                    existingMovie.setDuration(movie.getDuration());
                    existingMovie.setLanguages(movie.getLanguages());
                    existingMovie.setFormats(movie.getFormats());
                    existingMovie.updateTimestamps();
                    return movieRepository.save(existingMovie);
                });
    }

    public Mono<Void> deleteMovie(Long id) {
        return movieRepository.deleteById(id);
    }

    public Flux<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContaining(title);
    }

    public Flux<Movie> findMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    public Flux<Movie> findMoviesByReleaseDateRange(LocalDate fromDate, LocalDate toDate) {
        return movieRepository.findByReleaseDateBetween(fromDate, toDate);
    }

    public Flux<Movie> findUpcomingMovies() {
        return movieRepository.findUpcomingMovies();
    }

    public Flux<Movie> findNowShowingMovies() {
        return movieRepository.findReleasedMovies();
    }

    public Flux<Movie> findMoviesByFormat(String format) {
        return movieRepository.findByFormat(format);
    }

    public Flux<Movie> findMoviesByLanguage(String language) {
        return movieRepository.findByLanguage(language);
    }

    public Flux<Movie> findNowShowingInTheater(Long theaterId) {
        return movieRepository.findNowShowingInTheater(theaterId);
    }

    public Flux<Movie> findMoviesByFilters(String title, String genre, String language, String format) {
        // This is a simplified implementation
        // In a real application, you might want to build a dynamic query based on provided filters
        if (title != null) {
            return searchMoviesByTitle(title);
        } else if (genre != null) {
            return findMoviesByGenre(genre);
        } else if (language != null) {
            return findMoviesByLanguage(language);
        } else if (format != null) {
            return findMoviesByFormat(format);
        } else {
            return getAllMovies();
        }
    }
}
