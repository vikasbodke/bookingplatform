package com.bookingplatform.usecases.browsing.dto;

import java.time.LocalDate;


public class BrowseMovieSearchRequest {
    private Integer cityId;
    private String language;
    private Integer theaterId;
    private LocalDate date;
    private String format;
    private String genre;
    private Integer page;
    private Integer limit;

    public BrowseMovieSearchRequest() {
        this.page = 1;
        this.limit = 20;
    }

    public BrowseMovieSearchRequest(Integer cityId, String language, Integer theaterId,
                                    LocalDate date, String format, String genre,
                                    Integer page, Integer limit) {
        this.cityId = cityId;
        this.language = language;
        this.theaterId = theaterId;
        this.date = date;
        this.format = format;
        this.genre = genre;
        this.page = page != null ? page : 1;
        this.limit = limit != null ? limit : 20;
    }

    // Getters and Setters
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Integer theaterId) {
        this.theaterId = theaterId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page != null ? page : 1;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit != null ? limit : 20;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer cityId;
        private String language;
        private Integer theaterId;
        private LocalDate date;
        private String format;
        private String genre;
        private Integer page = 1;
        private Integer limit = 20;

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

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder page(Integer page) {
            this.page = page != null ? page : 1;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit != null ? limit : 20;
            return this;
        }

        public BrowseMovieSearchRequest build() {
            return new BrowseMovieSearchRequest(cityId, language, theaterId, date, format, genre, page, limit);
        }
    }
}
