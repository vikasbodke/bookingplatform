package com.bookingplatform.entities.showtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class ShowtimeAvailabilityService {

    private final ShowtimeAvailabilityRepository availabilityRepository;

    @Autowired
    public ShowtimeAvailabilityService(
            ShowtimeAvailabilityRepository availabilityRepository,
            ShowtimeService showtimeService) {
        this.availabilityRepository = availabilityRepository;
    }

    public Mono<ShowtimeAvailability> getAvailability(Long showtimeId) {
        return availabilityRepository.findByShowtimeId(showtimeId);
    }

    @Transactional
    public Mono<Boolean> reserveSeats(Long showtimeId, int seatsToReserve) {
        return availabilityRepository.reserveSeats(showtimeId, seatsToReserve)
                .map(updated -> updated > 0);
    }

    @Transactional
    public Mono<Boolean> releaseSeats(Long showtimeId, int seatsToRelease) {
        return availabilityRepository.releaseSeats(showtimeId, seatsToRelease)
                .map(updated -> updated > 0);
    }

    public Mono<Boolean> hasAvailableSeats(Long showtimeId, int requiredSeats) {
        return availabilityRepository.findByShowtimeId(showtimeId)
                .map(availability -> 
                    availability.getAvailableSeats() != null && 
                    availability.getAvailableSeats() >= requiredSeats
                )
                .defaultIfEmpty(false);
    }

    public Mono<ShowtimeAvailability> initializeAvailability(Showtime showtime, int totalSeats) {
        ShowtimeAvailability availability = new ShowtimeAvailability();
        availability.setShowtime(showtime);
        availability.setAvailableSeats(totalSeats);
        availability.setFullyBooked(false);
        return availabilityRepository.save(availability);
    }

    public Mono<Boolean> isFullyBooked(Long showtimeId) {
        return availabilityRepository.isFullyBooked(showtimeId)
                .defaultIfEmpty(false);
    }
}
