package com.bookingplatform.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class EmbeddedPostgresManager  implements DisposableBean {
    private final EmbeddedPostgres postgres;

    public EmbeddedPostgresManager() throws IOException {
        postgres = EmbeddedPostgres.builder().setPort(5432).start();
    }

    @Override
    public void destroy() throws IOException {
        postgres.close();
    }
}