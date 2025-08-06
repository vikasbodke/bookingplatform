package com.bookingplatform.entities.appuser;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<AppUser> allUsers() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<AppUser> getUser(@PathVariable("id")  Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<AppUser> createUser(@RequestBody AppUser user) {
        return service.save(user);
    }

    @PutMapping("/{id}")
    public Mono<AppUser> updateUser(@PathVariable("id")  Long id, @RequestBody AppUser user) {
        return service.save(user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable("id")  Long id) {
        return service.deleteById(id);
    }
}