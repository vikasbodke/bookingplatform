package com.bookingplatform.entities.appuser;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends ReactiveCrudRepository<AppUser, Long> {
}
