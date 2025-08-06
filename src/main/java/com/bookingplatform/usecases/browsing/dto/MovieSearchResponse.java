package com.bookingplatform.usecases.browsing.dto;

import com.bookingplatform.usecases.browsing.BrowseController;

import java.util.List;

public class MovieSearchResponse {
    private Integer cityId;
    private String date;
    private List<BrowseMovieResponse> movies;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BrowseMovieResponse> getMovies() {
        return movies;
    }

    public void setMovies(List<BrowseMovieResponse> movies) {
        this.movies = movies;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer cityId;
        private String date;
        private List<BrowseMovieResponse> movies;

        public Builder cityId(Integer cityId) {
            this.cityId = cityId;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder movies(List<BrowseMovieResponse> movies) {
            this.movies = movies;
            return this;
        }

        public MovieSearchResponse build() {
            MovieSearchResponse response = new MovieSearchResponse();
            response.cityId = this.cityId;
            response.date = this.date;
            response.movies = this.movies;
            return response;
        }
    }

}
