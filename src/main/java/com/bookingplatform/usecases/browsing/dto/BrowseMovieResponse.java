package com.bookingplatform.usecases.browsing.dto;

import java.util.List;

public class BrowseMovieResponse {
    private Integer movieId;
    private String title;
    private String genre;
    private List<String> languages;
    private List<String> formats;
    private List<BrowseTheaterInfo> theaters;

    public BrowseMovieResponse() {
    }

    public BrowseMovieResponse(Integer movieId, String title, String genre,
                               List<String> languages, List<String> formats,
                               List<BrowseTheaterInfo> theaters) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.languages = languages;
        this.formats = formats;
        this.theaters = theaters;
    }

    // Getters and Setters
    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    public List<BrowseTheaterInfo> getTheaters() {
        return theaters;
    }

    public void setTheaters(List<BrowseTheaterInfo> theaters) {
        this.theaters = theaters;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer movieId;
        private String title;
        private String genre;
        private List<String> languages;
        private List<String> formats;
        private List<BrowseTheaterInfo> theaters;

        public Builder movieId(Integer movieId) {
            this.movieId = movieId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder languages(List<String> languages) {
            this.languages = languages;
            return this;
        }

        public Builder formats(List<String> formats) {
            this.formats = formats;
            return this;
        }

        public Builder theaters(List<BrowseTheaterInfo> theaters) {
            this.theaters = theaters;
            return this;
        }

        public BrowseMovieResponse build() {
            return new BrowseMovieResponse(movieId, title, genre, languages, formats, theaters);
        }
    }
}
