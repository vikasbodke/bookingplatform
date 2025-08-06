package com.bookingplatform.entities.showtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    public Mono<Showtime> createShowtime(Showtime showtime) {
        return checkForConflicts(showtime)
            .flatMap(hasConflicts -> {
                if (hasConflicts) return Mono.error(new RuntimeException("Scheduling conflict"));
                showtime.updateTimestamps();
                return showtimeRepository.save(showtime);
            });
    }

    public Mono<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Flux<Showtime> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    public Flux<Showtime> getShowtimesByTheater(Long theaterId) {
        return showtimeRepository.findByTheaterId(theaterId);
    }

    public Flux<Showtime> getShowtimesByDateRange(LocalDateTime start, LocalDateTime end) {
        return showtimeRepository.findByTimeRange(start, end);
    }

    /**
     * Retrieves all active showtimes from the database.
     * @return Flux of all active showtimes
     */
    public Flux<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll()
                .filter(Showtime::getActive);
    }

    public Mono<Boolean> checkForConflicts(Showtime showtime) {
        return showtimeRepository
            .findConflictingShowtimes(
                showtime.getScreenId(),
                showtime.getStartTime(),
                showtime.getEndTime()
            )
            .hasElements();
    }

    public Mono<Showtime> updateShowtime(Long id, Showtime showtime) {
        return showtimeRepository.findById(id)
            .flatMap(existing -> {
                existing.setMovieId(showtime.getMovieId());
                existing.setTheaterId(showtime.getTheaterId());
                existing.setScreenId(showtime.getScreenId());
                existing.setStartTime(showtime.getStartTime());
                existing.setEndTime(showtime.getEndTime());
                existing.setLanguage(showtime.getLanguage());
                existing.setActive(showtime.getActive());
                existing.updateTimestamps();
                return showtimeRepository.save(existing);
            });
    }

    public Mono<Void> deleteShowtime(Long id) {
        return showtimeRepository.deleteById(id);
    }

    public Mono<Boolean> toggleStatus(Long id, boolean isActive) {
        return showtimeRepository.updateStatus(id, isActive)
            .map(updated -> updated > 0);
    }
}
