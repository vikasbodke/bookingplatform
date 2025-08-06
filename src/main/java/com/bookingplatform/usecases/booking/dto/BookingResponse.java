package com.bookingplatform.usecases.booking.dto;

import com.bookingplatform.usecases.booking.Booking;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class BookingResponse {
    private Long bookingId;
    private Long showtimeId;
    private Long screenId;
    private String bookingTime;
    private Double totalAmount;
    private String status;
    private List<SeatInfo> seats;

    public static BookingResponse fromBooking(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setShowtimeId(booking.getShowtimeId());
        response.setScreenId(booking.getScreenId());
        response.setBookingTime(booking.getBookingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus().name());
        // Note: You'll need to fetch and set the seat information separately
        return response;
    }

    public static class SeatInfo {
        private String rowLabel;
        private Integer seatNumber;

        public String getRowLabel() {
            return rowLabel;
        }

        public void setRowLabel(String rowLabel) {
            this.rowLabel = rowLabel;
        }

        public Integer getSeatNumber() {
            return seatNumber;
        }

        public void setSeatNumber(Integer seatNumber) {
            this.seatNumber = seatNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SeatInfo seatInfo = (SeatInfo) o;
            return Objects.equals(rowLabel, seatInfo.rowLabel) &&
                   Objects.equals(seatNumber, seatInfo.seatNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowLabel, seatNumber);
        }
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
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

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SeatInfo> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatInfo> seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingResponse that = (BookingResponse) o;
        return Objects.equals(bookingId, that.bookingId) &&
               Objects.equals(showtimeId, that.showtimeId) &&
               Objects.equals(screenId, that.screenId) &&
               Objects.equals(bookingTime, that.bookingTime) &&
               Objects.equals(totalAmount, that.totalAmount) &&
               Objects.equals(status, that.status) &&
               Objects.equals(seats, that.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, showtimeId, screenId, bookingTime, totalAmount, status, seats);
    }
}
