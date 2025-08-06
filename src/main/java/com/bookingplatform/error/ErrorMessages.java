package com.bookingplatform.error;

import com.bookingplatform.dto.ApiResponse;
import com.bookingplatform.usecases.booking.dto.BookingResponse;
import com.bookingplatform.usecases.browsing.dto.MovieSearchResponse;
import com.bookingplatform.usecases.browsing.dto.ShowtimeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ErrorMessages {
    public static final ResponseEntity<ApiResponse<MovieSearchResponse>> NO_MOVIES_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(
                    "NO_MOVIES_FOUND",
                    "No movies found for the given city and date."
            ));
    public static final Mono<ResponseEntity<ApiResponse<ShowtimeResponse>>> INVALID_DATE = Mono.just(ResponseEntity.badRequest()
            .body(ApiResponse.error(
                    "INVALID_DATE",
                    "Invalid date. Date must be today or in the future."
            )));
    public static final Mono<ResponseEntity<ApiResponse<ShowtimeResponse>>> INVALID_DATE_FORMAT = Mono.just(ResponseEntity.badRequest()
            .body(ApiResponse.error(
                    "INVALID_DATE_FORMAT",
                    "Invalid date format. Please use YYYY-MM-DD format."
            )));
    public static final Mono<ResponseEntity<ApiResponse<ShowtimeResponse>>> INTERNAL_SERVER_ERROR = Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(
                    "INTERNAL_SERVER_ERROR",
                    "An error occurred while searching for showtimes"
            )));
    public static final Mono<ResponseEntity<ApiResponse<ShowtimeResponse>>> NO_SHOWTIMES_FOUND = Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(
                    "NO_SHOWTIMES_FOUND",
                    "No showtimes found for the given criteria."
            )));
    public static final ResponseEntity<ApiResponse<BookingResponse>> BOOKING_NOT_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error("NO_BOOKING_FOUND", "Booking not found"));
    public static final Mono<ResponseEntity<ApiResponse<MovieSearchResponse>>> MONO_INVALID_DATE = Mono.just(ResponseEntity.badRequest()
            .body(ApiResponse.<MovieSearchResponse>error(
                    "INVALID_DATE",
                    "Invalid date. Date must be today or in the future."
            )));
    public static final Mono<ResponseEntity<ApiResponse<MovieSearchResponse>>> MONO_INVALID_DATE_FORMAT = Mono.just(ResponseEntity.badRequest()
            .body(ApiResponse.<MovieSearchResponse>error(
                    "INVALID_DATE_FORMAT",
                    "Invalid date format. Please use YYYY-MM-DD format."
            )));

    public static Mono<ResponseEntity<ApiResponse<BookingResponse>>> errorBookingNotFound(ResponseEntity.BodyBuilder status, String BOOKING_NOT_FOUND, String bookingId) {
        return Mono.just(status
                .body(ApiResponse.<BookingResponse>error(
                        BOOKING_NOT_FOUND,
                        bookingId
                )));
    }
}
