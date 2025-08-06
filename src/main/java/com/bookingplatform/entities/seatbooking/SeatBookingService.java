package com.bookingplatform.entities.seatbooking;

import com.bookingplatform.usecases.booking.BookingService;
import com.bookingplatform.entities.showtime.ShowtimeService;
import com.bookingplatform.usecases.booking.dto.BookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class SeatBookingService {

    private final SeatBookingRepository seatBookingRepository;

    @Autowired
    public SeatBookingService(
            SeatBookingRepository seatBookingRepository) {
        this.seatBookingRepository = seatBookingRepository;
    }

    @Transactional
    public Mono<UUID> bookSeat(SeatBooking seatBooking) {
        return checkSeatAvailability(
                seatBooking.getShowtimeId(),
                seatBooking.getRowLabel(),
                seatBooking.getSeatNumber()
            )
            .flatMap(available -> {
                if (!available) {
                    return Mono.error(new IllegalStateException("Seat is already booked"));
                }
                seatBooking.setStatus(SeatBooking.SeatStatus.BOOKED);
                seatBooking.setBookingReference(generateBookingReference());
                seatBooking.updateTimestamps();
                return seatBookingRepository.save(seatBooking)
                        .map(SeatBooking::getBookingReference);
            });
    }

    @Transactional
    public Mono<UUID> bookMultipleSeats(Long showtimeId, List<BookingRequest.SeatSelection> seatBookings) {
        if (seatBookings == null || seatBookings.isEmpty()) {
            return Mono.error(new IllegalArgumentException("No seat selections provided"));
        }

        // All seat bookings should be for the same showtime and booking
        UUID bookingRef = generateBookingReference();
        
        // Check all seats are available - fail fast if any seat is booked
        return Flux.fromIterable(seatBookings)
            .concatMap(seat -> checkSeatAvailability(showtimeId, seat.getRowLabel(), seat.getSeatNumber())
                .flatMap(available -> {
                    if (!available) {
                        return Mono.error(new IllegalStateException(
                            "Seat " + seat.getRowLabel() + seat.getSeatNumber() + " is already booked"));
                    }
                    return Mono.just(true);
                })
            )
            .then()
            .then(Mono.defer(() -> {
                // Save all seat bookings
                return Flux.fromIterable(seatBookings)
                    .flatMap(seatSelection -> {
                        SeatBooking seat = new SeatBooking();
                        seat.setShowtimeId(showtimeId);
                        seat.setRowLabel(seatSelection.getRowLabel());
                        seat.setSeatNumber(seatSelection.getSeatNumber());
                        seat.setStatus(SeatBooking.SeatStatus.BOOKED);
                        seat.setBookingReference(bookingRef);
                        seat.updateTimestamps();
                        return seatBookingRepository.save(seat);
                    })
                    .then(Mono.just(bookingRef));
            }));
    }

    public Mono<Boolean> checkSeatAvailability(Long showtimeId, String rowLabel, Integer seatNumber) {
        return seatBookingRepository.isSeatBooked(showtimeId, rowLabel, seatNumber)
                .map(booked -> !booked);
    }

    /**
     * Checks if any of the specified seats are already booked for the given showtime.
     * @param showtimeId The ID of the showtime
     * @param seatSelections List of seat selections (rowLabel and seatNumber)
     * @return Mono<Boolean> true if all seats are available, false if any seat is already booked
     */
    public Mono<Boolean> areSeatsAvailable(Long showtimeId, List<BookingRequest.SeatSelection> seatSelections) {
        if (seatSelections == null || seatSelections.isEmpty()) {
            return Mono.just(true);
        }

        return Flux.fromIterable(seatSelections)
                .flatMap(seat -> seatBookingRepository.isSeatBooked(
                        showtimeId,
                        seat.getRowLabel(),
                        seat.getSeatNumber()
                ))
                .any(booked -> booked)  // Check if any seat is booked
                .map(anyBooked -> !anyBooked);  // Invert to return true if all available
    }



    public Flux<SeatBooking> getSeatBookingsByShowtime(Long showtimeId) {
        return seatBookingRepository.findByShowtimeId(showtimeId);
    }

    public Mono<SeatBooking> findSeatBookingById(Long seatBookingId) {
        return seatBookingRepository.findById(seatBookingId);
    }
    
    /**
     * Generate a new booking reference
     * @return A new unique booking reference
     */
    public UUID generateBookingReference() {
        return UUID.randomUUID();
    }

}
