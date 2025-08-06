package com.bookingplatform.dto;


public class ApiResponse<T> {
    private String status;
    private T data;
    private ErrorResponse error;

    public ApiResponse() {
    }

    public ApiResponse(String status, T data, ErrorResponse error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data, null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>("error", null,
                new ErrorResponse(code, message));
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    // Builder
    public static <T> ApiResponse.Builder<T> builder() {
        return new ApiResponse.Builder<>();
    }

    public static class Builder<T> {
        private String status;
        private T data;
        private ErrorResponse error;

        public ApiResponse.Builder<T> status(String status) {
            this.status = status;
            return this;
        }

        public ApiResponse.Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponse.Builder<T> error(ErrorResponse error) {
            this.error = error;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(status, data, error);
        }
    }
}
