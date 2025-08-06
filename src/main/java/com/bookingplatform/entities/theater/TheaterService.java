package com.bookingplatform.entities.theater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public Mono<Theater> createTheater(Theater theater) {
        theater.updateTimestamps();
        return theaterRepository.save(theater);
    }

    public Mono<Theater> getTheaterById(Long id) {
        return theaterRepository.findById(id);
    }

    public Flux<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Mono<Theater> updateTheater(Long id, Theater theater) {
        return theaterRepository.findById(id)
                .flatMap(existingTheater -> {
                    existingTheater.setName(theater.getName());
                    existingTheater.setLocation(theater.getLocation());
                    existingTheater.setCityId(theater.getCityId());
                    existingTheater.setPartnerId(theater.getPartnerId());
                    existingTheater.setNumberOfScreens(theater.getNumberOfScreens());
                    existingTheater.setContactPhone(theater.getContactPhone());
                    existingTheater.setActive(theater.getActive());
                    existingTheater.updateTimestamps();
                    return theaterRepository.save(existingTheater);
                });
    }

    public Mono<Void> deleteTheater(Long id) {
        return theaterRepository.deleteById(id);
    }

    public Mono<Boolean> toggleTheaterStatus(Long id, boolean isActive) {
        return theaterRepository.updateStatus(id, isActive)
                .map(updated -> updated > 0);
    }

    public Flux<Theater> findTheatersByCity(Long cityId) {
        return theaterRepository.findByCityId(cityId);
    }

    public Flux<Theater> findTheatersByPartner(Long partnerId) {
        return theaterRepository.findByPartnerId(partnerId);
    }

    public Flux<Theater> searchTheaters(String name) {
        return theaterRepository.findByNameContaining(name);
    }

    public Flux<Theater> findActiveTheaters(boolean isActive) {
        return theaterRepository.findByActiveStatus(isActive);
    }

    public Flux<Theater> findTheatersByCityAndName(Long cityId, String name) {
        return theaterRepository.findByCityAndName(cityId, name);
    }
}
