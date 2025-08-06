package com.bookingplatform.usecases.booking;

import com.bookingplatform.entities.seatbooking.SeatBookingService;
import com.bookingplatform.usecases.booking.dto.BookingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final CacheManager cacheManager;
    private final SeatBookingService seatBookingService;

    public BookingService(BookingRepository bookingRepository,
                          CacheManager cacheManager,
                          SeatBookingService seatBookingService) {
        this.bookingRepository = bookingRepository;
        this.cacheManager = cacheManager;
        this.seatBookingService = seatBookingService;
    }

    /**
     * Creates a booking and books the specified seats.
     *
     * @param userId       The ID of the user making the booking
     * @param showtimeId   The ID of the showtime
     * @param totalAmount  The total amount for the booking
     * @param seatHash     A unique hash representing the selected seats
     * @param seatBookings List of seat bookings to be made
     * @return Mono containing the created booking
     */
    @Transactional
    public Mono<Booking> createBooking(Long userId, Long showtimeId, Double totalAmount,
                                       String seatHash, List<BookingRequest.SeatSelection> seatBookings) {
        // 1. First check if user already has a booking for this showtime based on userId and showtimeId
        // 2. Check if the requested seats are available for this showtime
        // 3. Book the requested seats
        // 4. Save the booking

        return bookingRepository.existsByUserIdAndShowtimeId(userId, showtimeId)
                .flatMap(exists -> {
                    if (exists) {
                        LOG.error("User {} already has a booking for showtime {}", userId, showtimeId);
                        return Mono.error(new IllegalStateException("User already has a booking for this showtime"));
                    }

                    // First, check if all requested seats are available
                    return seatBookingService.areSeatsAvailable(showtimeId, seatBookings)
                            .flatMap(seatsAvailable -> {
                                if (!seatsAvailable) {
                                    LOG.error("One or more seats are already booked for showtime {}", showtimeId);
                                    return Mono.error(new IllegalStateException("One or more selected seats are already booked"));
                                }

                                // First, try to book all seats
                                return seatBookingService.bookMultipleSeats(showtimeId, seatBookings)
                                        .onErrorResume(error -> {
                                            LOG.error("Failed to book seats for showtime {}: {}", showtimeId, error.getMessage(), error);
                                            return Mono.error(new IllegalStateException("Failed to book seats. Please try again.", error));
                                        })
                                        .flatMap(seatBookingReference -> {
                                            // Create and save the booking with the booking reference
                                            Booking booking = getBookingReq(userId, showtimeId, totalAmount, seatHash);
                                            booking.setSeatBookingReference(seatBookingReference);

                                            return bookingRepository.save(booking)
                                                    .onErrorResume(error -> {
                                                        // If booking save fails after seats are booked, log the error
                                                        LOG.error("Failed to save booking after seats were reserved for user {} and showtime {}: {}",
                                                            userId, showtimeId, error.getMessage(), error);
                                                        return Mono.error(new IllegalStateException("Failed to complete booking. Please contact support.", error));
                                                    });
                                        });
                            });
                });
    }

    /**
     * Overloaded method for backward compatibility
     */
    @Transactional
    public Mono<Booking> createBooking(Long userId, Long showtimeId, Double totalAmount, String seatHash) {
        return createBooking(userId, showtimeId, totalAmount, seatHash, List.of());
    }

    private static String getSeatingHashKey(Long showtimeId, String seatHash) {
        // Generate a cache key using showtimeId and seatHash
        return showtimeId + "_" + seatHash;
    }

    private static Booking getBookingReq(Long userId, Long showtimeId, Double totalAmount, String seatHash) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setShowtimeId(showtimeId);
        booking.setSeatHash(seatHash);
        booking.setBookingTime(LocalDateTime.now());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(Booking.BookingStatus.PENDING);
        return booking;
    }


    @Transactional
    public Mono<Void> cancelBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .flatMap(booking -> {
                    if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
                        return Mono.empty(); // Already cancelled
                    }

                    booking.setStatus(Booking.BookingStatus.CANCELLED);
                    return bookingRepository.save(booking).then();
                });
    }

    public Flux<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Mono<Booking> getBookingDetails(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }
}
