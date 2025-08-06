package com.bookingplatform.usecases.booking;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Table("bookings")
public class Booking {
    
    @Id
    @Column("booking_id")
    private Long id;
    
    @Column("user_id")
    private Long userId;

    @Column("showtime_id")
    private Long showtimeId;
    
    @Nullable
    private Long screenId;
    
    @Column("seat_booking_reference")
    private UUID seatBookingReference;
    
    @Column("seat_hash")
    private String seatHash;
    
    private LocalDateTime bookingTime;
    private Double totalAmount;
    private BookingStatus status;
    
    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED;
        
        public static BookingStatus fromString(String value) {
            if (value == null) return null;
            return valueOf(value.toUpperCase());
        }
    }

    public Booking() {
        // Default constructor for JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }
    
    public UUID getSeatBookingReference() {
        return seatBookingReference;
    }
    
    public void setSeatBookingReference(UUID seatBookingReference) {
        this.seatBookingReference = seatBookingReference;
    }

    public String getSeatHash() {
        return seatHash;
    }

    public void setSeatHash(String seatHash) {
        this.seatHash = seatHash;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) &&
               Objects.equals(userId, booking.userId) &&
               Objects.equals(showtimeId, booking.showtimeId) &&
               Objects.equals(screenId, booking.screenId) &&
               Objects.equals(seatBookingReference, booking.seatBookingReference) &&
               Objects.equals(seatHash, booking.seatHash) &&
               Objects.equals(bookingTime, booking.bookingTime) &&
               Objects.equals(totalAmount, booking.totalAmount) &&
               status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, showtimeId, screenId, seatBookingReference, seatHash, bookingTime, totalAmount, status);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId=" + userId +
                ", showtimeId=" + showtimeId +
                ", screenId=" + screenId +
                ", seatBookingReference='" + seatBookingReference + '\'' +
                ", seatHash='" + seatHash + '\'' +
                ", bookingTime=" + bookingTime +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}
