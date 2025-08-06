package com.bookingplatform.entities.movie;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("movies")
public class Movie {
    
    @Id
    @Column("movie_id")
    private Long movieId;
    
    @Column("title")
    private String title;
    
    @Column("genre")
    private String genre;
    
    @Column("release_date")
    private LocalDate releaseDate;
    
    @Column("duration")
    private Integer duration; // in minutes
    
    @Column("languages")
    private JsonNode languages; // JSON array of available languages
    
    @Column("formats")
    private JsonNode formats; // JSON array of formats (2D, 3D, IMAX)
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public Movie() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
        this.updatedAt = LocalDateTime.now();
    }

    public JsonNode getLanguages() {
        return languages;
    }

    public void setLanguages(JsonNode languages) {
        this.languages = languages;
        this.updatedAt = LocalDateTime.now();
    }

    public JsonNode getFormats() {
        return formats;
    }

    public void setFormats(JsonNode formats) {
        this.formats = formats;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateTimestamps() {
        this.updatedAt = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = this.updatedAt;
        }
    }
}
