package com.bookingplatform.usecases.browsing.dto;

import java.time.LocalDate;

public class ShowtimeSearchRequest {
    private final Integer movieId;
    private final Integer cityId;
    private final String language;
    private final Integer theaterId;
    private final LocalDate date;
    private final String format;
    private final int page;
    private final int limit;

    private ShowtimeSearchRequest(Builder builder) {
        this.movieId = builder.movieId;
        this.cityId = builder.cityId;
        this.language = builder.language;
        this.theaterId = builder.theaterId;
        this.date = builder.date != null ? builder.date : LocalDate.now();
        this.format = builder.format;
        this.page = builder.page > 0 ? builder.page : 1;
        this.limit = builder.limit > 0 ? Math.min(builder.limit, 50) : 20;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getTheaterId() {
        return theaterId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getFormat() {
        return format;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return (page - 1) * limit;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer movieId;
        private Integer cityId;
        private String language;
        private Integer theaterId;
        private LocalDate date;
        private String format;
        private int page = 1;
        private int limit = 20;

        public Builder movieId(Integer movieId) {
            this.movieId = movieId;
            return this;
        }

        public Builder cityId(Integer cityId) {
            this.cityId = cityId;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder theaterId(Integer theaterId) {
            this.theaterId = theaterId;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public ShowtimeSearchRequest build() {
            if (movieId == null || cityId == null) {
                throw new IllegalStateException("Movie ID and City ID are required");
            }
            return new ShowtimeSearchRequest(this);
        }
    }
}
