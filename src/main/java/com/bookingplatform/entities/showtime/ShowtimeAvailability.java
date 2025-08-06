package com.bookingplatform.entities.showtime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("showtime_availability")
public class ShowtimeAvailability {
    
    @Id
    @Column("availability_id")
    private Long availabilityId;
    
    @Column("showtime_id")
    private Long showtimeId;
    private transient Showtime showtime;
    
    @Column("available_seats")
    private Integer availableSeats;
    
    @Column("is_fully_booked")
    private Boolean isFullyBooked;
    
    @Column("last_updated")
    private LocalDateTime lastUpdated;

    public ShowtimeAvailability() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
        this.lastUpdated = LocalDateTime.now();
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
        if (showtime != null) {
            this.showtimeId = showtime.getShowtimeId();
        }
        this.lastUpdated = LocalDateTime.now();
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
        this.isFullyBooked = availableSeats != null && availableSeats <= 0;
        this.lastUpdated = LocalDateTime.now();
    }

    public Boolean getFullyBooked() {
        return isFullyBooked;
    }

    public void setFullyBooked(Boolean fullyBooked) {
        isFullyBooked = fullyBooked;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void updateTimestamps() {
        this.lastUpdated = LocalDateTime.now();
    }
}
