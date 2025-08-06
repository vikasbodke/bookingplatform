package com.bookingplatform.usecases.browsing.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ShowtimeInfo {
    private final Long showtimeId;
    private final MovieInfo movie;
    private final TheaterInfo theater;
    private final ScreenInfo screen;
    private final String language;
    private final String format;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Integer availableCapacity;

    private ShowtimeInfo(Builder builder) {
        this.showtimeId = builder.showtimeId;
        this.movie = builder.movie;
        this.theater = builder.theater;
        this.screen = builder.screen;
        this.language = builder.language;
        this.format = builder.format;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.availableCapacity = builder.availableCapacity;
    }

    public MovieInfo getMovie() {
        return movie;
    }

    public TheaterInfo getTheater() {
        return theater;
    }

    public ScreenInfo getScreen() {
        return screen;
    }

    public String getLanguage() {
        return language;
    }

    public String getFormat() {
        return format;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Integer getAvailableCapacity() {
        return availableCapacity;
    }

    // Backward compatibility methods
    public String getMovieTitle() {
        return movie != null ? movie.getTitle() : null;
    }

    public List<String> getMovieLanguages() {
        return movie != null ? movie.getLanguages() : List.of();
    }

    public Integer getTheaterId() {
        return theater != null ? theater.getTheaterId().intValue() : null;
    }

    public String getTheaterName() {
        return theater != null ? theater.getName() : null;
    }

    public Integer getScreenNumber() {
        return screen != null ? screen.getScreenNumber() : null;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long showtimeId;
        private MovieInfo movie;
        private TheaterInfo theater;
        private ScreenInfo screen;
        private String language;
        private String format;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer availableCapacity;

        public Builder movie(MovieInfo movie) {
            this.movie = movie;
            return this;
        }

        public Builder theater(TheaterInfo theater) {
            this.theater = theater;
            return this;
        }

        public Builder screen(ScreenInfo screen) {
            this.screen = screen;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder availableCapacity(Integer availableCapacity) {
            this.availableCapacity = availableCapacity;
            return this;
        }

        // For backward compatibility
        public Builder movieTitle(String title) {
            if (this.movie == null) {
                this.movie = MovieInfo.builder().title(title).build();
            } else {
                this.movie = MovieInfo.builder()
                    .title(title)
                    .languages(this.movie.getLanguages())
                    .build();
            }
            return this;
        }

        public Builder movieLanguages(List<String> movieLanguages) {
            if (this.movie == null) {
                this.movie = MovieInfo.builder()
                    .languages(movieLanguages)
                    .build();
            } else {
                this.movie = MovieInfo.builder()
                    .title(this.movie.getTitle())
                    .languages(movieLanguages)
                    .build();
            }
            return this;
        }

        public Builder showtimeId(Long showtimeId) {
            this.showtimeId = showtimeId;
            return this;
        }

        public ShowtimeInfo build() {
            if (showtimeId == null ) {
                throw new IllegalStateException("Showtime ID is required");
            }
            return new ShowtimeInfo(this);
        }
    }
}
