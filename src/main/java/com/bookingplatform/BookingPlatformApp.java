package com.bookingplatform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class BookingPlatformApp {
    private static final Logger logger = LogManager.getLogger(BookingPlatformApp.class);

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(BookingPlatformApp.class);
            Environment env = app.run(args).getEnvironment();
            
            String protocol = "http";
            if (env.getProperty("server.ssl.key-store") != null) {
                protocol = "https";
            }
            
            logger.info("\n----------------------------------------------------------\n\t" +
                    "Application '{}' is running! Access URLs:\n\t" +
                    "Local: \t\t{}://localhost:{}{}\n\t" +
                    "Profile(s): \t{}\n" +
                    "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path", ""),
                env.getActiveProfiles());
                
            logger.info("Application started successfully with Java version: {}", System.getProperty("java.version"));
            logger.debug("Debug logging is enabled");
        } catch (Exception e) {
            logger.error("Application startup failed: ", e);
            throw e;
        }
    }
}
