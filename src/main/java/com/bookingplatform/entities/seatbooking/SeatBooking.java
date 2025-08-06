package com.bookingplatform.entities.seatbooking;

import com.bookingplatform.usecases.booking.Booking;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("seat_bookings")
public class SeatBooking {
    
    @Id
    @Column("seat_booking_id")
    private Long seatBookingId;
    
    private transient Booking booking;
    
    @Column("showtime_id")
    private Long showtimeId;
    
    @Column("row_label")
    private String rowLabel;
    
    @Column("seat_number")
    private Integer seatNumber;
    
    @Column("status")
    private SeatStatus status;
    
    @Column("booking_reference")
    private UUID bookingReference;
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public enum SeatStatus {
        BOOKED,
        CANCELLED,
        COMPLETED
    }

    public SeatBooking() {
        this.status = SeatStatus.BOOKED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getSeatBookingId() {
        return seatBookingId;
    }

    public void setSeatBookingId(Long seatBookingId) {
        this.seatBookingId = seatBookingId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
        this.updatedAt = LocalDateTime.now();
    }



    public String getRowLabel() {
        return rowLabel;
    }

    public void setRowLabel(String rowLabel) {
        this.rowLabel = rowLabel;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
        this.updatedAt = LocalDateTime.now();
    }



    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public UUID getBookingReference() {
        return bookingReference;
    }
    
    public void setBookingReference(UUID bookingReference) {
        this.bookingReference = bookingReference;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public String toString() {
        return "SeatBooking{" +
               "seatBookingId=" + seatBookingId +
               ", bookingReference='" + bookingReference + '\'' +
               ", showtimeId=" + showtimeId +
               ", rowLabel='" + rowLabel + '\'' +
               ", seatNumber=" + seatNumber +
               ", status=" + status +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }

    public void updateTimestamps() {
        this.updatedAt = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = this.updatedAt;
        }
    }
}
