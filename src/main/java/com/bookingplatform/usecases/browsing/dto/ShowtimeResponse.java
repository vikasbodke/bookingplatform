package com.bookingplatform.usecases.browsing.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ShowtimeResponse {
    private final Integer movieId;
    private final String title;
    private final List<String> languages;
    private final String date;
    private final List<ShowtimeInfo> showtimes;

    private ShowtimeResponse(Builder builder) {
        this.movieId = builder.movieId;
        this.title = builder.title;
        this.languages = builder.languages;
        this.date = builder.date;
        this.showtimes = builder.showtimes;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getDate() {
        return date;
    }

    public List<ShowtimeInfo> getShowtimes() {
        return showtimes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer movieId;
        private String title;
        private List<String> languages;
        private String date;
        private List<ShowtimeInfo> showtimes;

        public Builder movieId(Integer movieId) {
            this.movieId = movieId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder languages(List<String> languages) {
            this.languages = languages;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder showtimes(List<ShowtimeInfo> showtimes) {
            this.showtimes = showtimes;
            return this;
        }

        public ShowtimeResponse build() {
            if (movieId == null || title == null || showtimes == null) {
                throw new IllegalStateException("Movie ID, title, and showtimes are required");
            }
            return new ShowtimeResponse(this);
        }
    }
}
