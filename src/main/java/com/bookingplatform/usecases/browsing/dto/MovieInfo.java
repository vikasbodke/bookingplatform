package com.bookingplatform.usecases.browsing.dto;

import java.util.List;
import java.util.Objects;

public class MovieInfo {
    private final Long movieId;
    private final String title;
    private final List<String> languages;
    private final String format;
    private final Integer duration; // in minutes
    private final String rating;

    private MovieInfo(Builder builder) {
        this.movieId = builder.movieId;
        this.title = builder.title;
        this.languages = builder.languages != null ? List.copyOf(builder.languages) : List.of();
        this.format = builder.format;
        this.duration = builder.duration;
        this.rating = builder.rating;
    }

    // Getters
    public Long getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getFormat() {
        return format;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getRating() {
        return rating;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieInfo movieInfo = (MovieInfo) o;
        return Objects.equals(movieId, movieInfo.movieId) &&
               Objects.equals(title, movieInfo.title) &&
               Objects.equals(languages, movieInfo.languages) &&
               Objects.equals(format, movieInfo.format) &&
               Objects.equals(duration, movieInfo.duration) &&
               Objects.equals(rating, movieInfo.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, title, languages, format, duration, rating);
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
               "movieId=" + movieId +
               ", title='" + title + '\'' +
               ", languages=" + languages +
               ", format='" + format + '\'' +
               ", duration=" + duration +
               ", rating='" + rating + '\'' +
               '}';
    }

    public static final class Builder {
        private Long movieId;
        private String title;
        private List<String> languages;
        private String format;
        private Integer duration;
        private String rating;

        private Builder() {
        }

        public Builder movieId(Long movieId) {
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

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public Builder rating(String rating) {
            this.rating = rating;
            return this;
        }

        public MovieInfo build() {
            if (movieId == null || title == null) {
                throw new IllegalStateException("Movie ID and title are required");
            }
            return new MovieInfo(this);
        }
    }
}
