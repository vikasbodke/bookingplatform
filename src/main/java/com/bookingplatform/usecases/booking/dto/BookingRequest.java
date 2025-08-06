package com.bookingplatform.usecases.booking.dto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BookingRequest {
    private Long showtimeId;
    private List<SeatSelection> selectedSeats;
    
    public static class SeatSelection {
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
            SeatSelection that = (SeatSelection) o;
            return Objects.equals(rowLabel, that.rowLabel) &&
                   Objects.equals(seatNumber, that.seatNumber);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(rowLabel, seatNumber);
        }
    }
    
    public Long getShowtimeId() {
        return showtimeId;
    }
    
    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }
    
    public List<SeatSelection> getSelectedSeats() {
        return selectedSeats;
    }
    
    public void setSelectedSeats(List<SeatSelection> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingRequest that = (BookingRequest) o;
        return Objects.equals(showtimeId, that.showtimeId) &&
               Objects.equals(selectedSeats, that.selectedSeats);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(showtimeId, selectedSeats);
    }


    public static Comparator<SeatSelection> SEAT_SELECTION_COMPARATOR = Comparator.comparing(SeatSelection::getRowLabel)
            .thenComparingInt(SeatSelection::getSeatNumber);

    public String getSeatsHash() {
        StringBuilder sb = new StringBuilder();
        selectedSeats.sort(SEAT_SELECTION_COMPARATOR);
        for (SeatSelection seat : selectedSeats) {
            sb.append(seat.getRowLabel());
            sb.append(seat.getSeatNumber());
        }
        return sb.toString();
    }
}
