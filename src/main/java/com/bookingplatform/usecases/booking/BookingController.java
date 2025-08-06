package com.bookingplatform.usecases.booking;

import com.bookingplatform.dto.ApiResponse;
import com.bookingplatform.error.ErrorMessages;
import com.bookingplatform.usecases.booking.dto.BookingRequest;
import com.bookingplatform.usecases.booking.dto.BookingResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static com.bookingplatform.dto.ApiResponse.success;
import static com.bookingplatform.usecases.booking.dto.BookingResponse.fromBooking;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger LOG = LogManager.getLogger(BookingController.class);
    public static final Mono<ResponseEntity<ApiResponse<BookingResponse>>> INTERNAL_SERVER_ERROR = Mono.just(ResponseEntity.internalServerError()
            .body(ApiResponse.<BookingResponse>error(
                    "INTERNAL_SERVER_ERROR",
                    "An error occurred while fetching the booking"
            )));

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<BookingResponse>>> createBooking(
            @RequestBody BookingRequest request) {
        LOG.info("Received request to create booking for showtime: {}", 
                request.getShowtimeId());
        
        try {
            double totalAmount = calculateTotalAmount(request);
            
            // For now, we'll use a hardcoded user ID since we're not using authentication
            Long userId = 1L; // Default user ID
            String seatHash = request.getSeatsHash();

            return bookingService.createBooking(userId, request.getShowtimeId(), totalAmount, seatHash, request.getSelectedSeats())
                    .map(successBookingResponse())
                    .onErrorResume(e -> {
                        LOG.error("Error creating booking", e);
                        return ErrorMessages.errorBookingNotFound(ResponseEntity.badRequest(), "BOOKING_CREATION_FAILED", "Failed to create booking: " + e.getMessage());
                    });
        } catch (Exception e) {
            LOG.error("Unexpected error in createBooking", e);
            return ErrorMessages.errorBookingNotFound(ResponseEntity.internalServerError(), "INTERNAL_SERVER_ERROR", "An unexpected error occurred while processing your request");
        }
    }

    private static Function<Booking, ResponseEntity<ApiResponse<BookingResponse>>> successBookingResponse() {
        return booking -> ResponseEntity.status(HttpStatus.CREATED)
                .body(success(fromBooking(booking)));
    }

    @GetMapping("/{bookingId}")
    public Mono<ResponseEntity<ApiResponse<BookingResponse>>> getBooking(
            @PathVariable Long bookingId) {
        LOG.info("Fetching booking with ID: {}", bookingId);
        
        return bookingService.getBookingDetails(bookingId)
                .map(booking -> ResponseEntity.ok(success(fromBooking(booking))))
                .switchIfEmpty(ErrorMessages.errorBookingNotFound(ResponseEntity.status(404), "BOOKING_NOT_FOUND", "Booking with ID " + bookingId + " not found"))
                .onErrorResume(e -> {
                    LOG.error("Error fetching booking with ID: {}", bookingId, e);
                    return INTERNAL_SERVER_ERROR;
                });
    }

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<BookingResponse>>>> getUserBookings() {
        // For now, we'll use a hardcoded user ID since we're not using authentication
        Long userId = 1L; // Default user ID
        LOG.info("Fetching bookings for user ID: {}", userId);
        
        return bookingService.getUserBookings(userId)
                .map(BookingResponse::fromBooking)
                .collectList()
                .map(bookings -> ResponseEntity.ok(success(bookings)))
                .onErrorResume(e -> {
                    LOG.error("Error fetching bookings for user ID: " + userId, e);
                    return Mono.just(ResponseEntity.internalServerError()
                            .body(ApiResponse.<List<BookingResponse>>error(
                                    "INTERNAL_SERVER_ERROR",
                                    "An error occurred while fetching your bookings"
                            )));
                });
    }


    @DeleteMapping("/{bookingId}")
    public Mono<ResponseEntity<ApiResponse<Void>>> cancelBooking(
            @PathVariable Long bookingId) {
        LOG.info("Cancelling booking with ID: {}", bookingId);
        
        return bookingService.cancelBooking(bookingId)
                .then(Mono.just(ResponseEntity.ok(ApiResponse.<Void>success(null))))
                .onErrorResume(e -> {
                    LOG.error("Error cancelling booking with ID: " + bookingId, e);
                    if (e instanceof IllegalStateException) {
                        return Mono.just(ResponseEntity.badRequest()
                                .body(ApiResponse.<Void>error(
                                        "CANCELLATION_FAILED",
                                        "Failed to cancel booking: " + e.getMessage()
                                )));
                    }
                    return errorBookingNotFound(bookingId);
                });
    }

    private static Mono<ResponseEntity<ApiResponse<Void>>> errorBookingNotFound(Long bookingId) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>error(
                        "BOOKING_NOT_FOUND",
                        "Booking with ID " + bookingId + " not found"
                )));
    }

    private double calculateTotalAmount(BookingRequest request) {
        // In a real application, you would calculate the total amount based on:
        // 1. Number of seats
        // 2. Seat type (premium, standard, etc.)
        // 3. Any applicable discounts
        // For now, we'll use a placeholder value
        return request.getSelectedSeats().size() * 10.0; // $10 per seat as an example
    }


}
