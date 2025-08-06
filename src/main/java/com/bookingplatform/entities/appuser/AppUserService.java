package com.bookingplatform.entities.appuser;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AppUserService {

    private final AppUserRepository repository;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    @Cacheable("usersCache")
    public Flux<AppUser> findAll() {
        return repository.findAll();
    }
    @Cacheable("usersCache")
    public Mono<AppUser> findById(Long id) {
        return repository.findById(id);
    }

    public Mono<AppUser> save(AppUser user) {
        return repository.save(user);
    }

    @CacheEvict("usersCache")
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}
