package com.bookingplatform;

import com.bookingplatform.entities.appuser.AppUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.test.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiIntegrationTest {
    private static final Logger logger = LogManager.getLogger(UserApiIntegrationTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldFetchInitialUsersFromDataSql() {
        List<AppUser> users = webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AppUser.class)
                .returnResult()
                .getResponseBody();

        assertThat(users)
                .isNotNull()
                .isNotEmpty();

        logger.info("Fetched initial users: {}", users);
    }


    private AppUser createRandomUser() {
        String randomName = "Charlie " + RandomStringUtils.randomAlphabetic(5);
        String randomEmail = "charlie" + RandomStringUtils.randomAlphanumeric(5) + "@example.com";
        AppUser randomUser = new AppUser();
        randomUser.setName(randomName);
        randomUser.setEmail(randomEmail);
        return randomUser;
    }

    @Test
    void shouldCreate() {
        AppUser newUser = createRandomUser();

        // Create user
        AppUser created = webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AppUser.class)
                .returnResult()
                .getResponseBody();

        assertThat(created).isNotNull();
        assertThat(created.getUserId()).isNotNull();
        assertThat(created.getName()).isEqualTo(newUser.getName());

    }

    @Test
    void shouldUpdateUser() {
        // Create a user first
        AppUser newUser = createRandomUser();

        AppUser created = webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AppUser.class)
                .returnResult()
                .getResponseBody();

        assertThat(created).isNotNull();

        String newEmail = "dave"+ RandomStringUtils.randomAlphabetic(3)+"@new.com";
        // Update the user
        created.setEmail(newEmail);

        webTestClient.put()
                .uri("/api/v1/users/{id}", created.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(created)
                .exchange()
                .expectStatus().isOk();

        // Fetch to verify
        AppUser updated = webTestClient.get()
                .uri("/api/v1/users/{id}", created.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AppUser.class)
                .returnResult()
                .getResponseBody();

        assertThat(updated).isNotNull();
        assertThat(updated.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void shouldDeleteUser() {
        // Create a user first
        AppUser newUser = createRandomUser();

        AppUser created = webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AppUser.class)
                .returnResult()
                .getResponseBody();

        assertThat(created).isNotNull();

        // Delete user
        webTestClient.delete()
                .uri("/api/v1/users/{id}", created.getUserId())
                .exchange()
                .expectStatus().isOk();

    }
}
