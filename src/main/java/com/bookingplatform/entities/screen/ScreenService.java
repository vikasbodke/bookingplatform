package com.bookingplatform.entities.screen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;

    @Autowired
    public ScreenService(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    public Mono<Screen> createScreen(Screen screen) {
        screen.updateTimestamps();
        return screenRepository.save(screen);
    }

    public Mono<Screen> getScreenById(Long id) {
        return screenRepository.findById(id);
    }

    public Flux<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    public Mono<Screen> updateScreen(Long id, Screen screen) {
        return screenRepository.findById(id)
                .flatMap(existingScreen -> {
                    existingScreen.setTheaterId(screen.getTheaterId());
                    existingScreen.setScreenNumber(screen.getScreenNumber());
                    existingScreen.setCapacity(screen.getCapacity());
                    existingScreen.setAudioType(screen.getAudioType());
                    existingScreen.setFormat(screen.getFormat());
                    existingScreen.setActive(screen.getActive());
                    existingScreen.updateTimestamps();
                    return screenRepository.save(existingScreen);
                });
    }

    public Mono<Void> deleteScreen(Long id) {
        return screenRepository.deleteById(id);
    }

    public Mono<Boolean> toggleScreenStatus(Long id, boolean isActive) {
        return screenRepository.updateStatus(id, isActive)
                .map(updated -> updated > 0);
    }

    public Flux<Screen> findScreensByTheater(Long theaterId) {
        return screenRepository.findByTheaterId(theaterId);
    }

    public Flux<Screen> findScreensByFormat(String format) {
        return screenRepository.findByFormat(format);
    }

    public Flux<Screen> findActiveScreens(boolean isActive) {
        return screenRepository.findByActiveStatus(isActive);
    }

    public Flux<Screen> findScreensByTheaterAndFormat(Long theaterId, String format) {
        return screenRepository.findByTheaterAndFormat(theaterId, format);
    }

    public Mono<Screen> findScreenByTheaterAndNumber(Long theaterId, Integer screenNumber) {
        return screenRepository.findByTheaterAndScreenNumber(theaterId, screenNumber);
    }
}
