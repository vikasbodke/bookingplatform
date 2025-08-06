package com.bookingplatform.usecases.browsing.dto;

import java.util.Objects;

public class ScreenInfo {
    private final Long screenId;
    private final Integer screenNumber;
    private final String screenType;
    private final Integer totalSeats;
    private final Long theaterId;

    private ScreenInfo(Builder builder) {
        this.screenId = builder.screenId;
        this.screenNumber = builder.screenNumber;
        this.screenType = builder.screenType;
        this.totalSeats = builder.totalSeats;
        this.theaterId = builder.theaterId;
    }

    // Getters
    public Long getScreenId() {
        return screenId;
    }

    public Integer getScreenNumber() {
        return screenNumber;
    }

    public String getScreenType() {
        return screenType;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenInfo that = (ScreenInfo) o;
        return Objects.equals(screenId, that.screenId) &&
               Objects.equals(screenNumber, that.screenNumber) &&
               Objects.equals(screenType, that.screenType) &&
               Objects.equals(totalSeats, that.totalSeats) &&
               Objects.equals(theaterId, that.theaterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenId, screenNumber, screenType, totalSeats, theaterId);
    }

    @Override
    public String toString() {
        return "ScreenInfo{" +
               "screenId=" + screenId +
               ", screenNumber=" + screenNumber +
               ", screenType='" + screenType + '\'' +
               ", totalSeats=" + totalSeats +
               ", theaterId=" + theaterId +
               '}';
    }

    public static final class Builder {
        private Long screenId;
        private Integer screenNumber;
        private String screenType;
        private Integer totalSeats;
        private Long theaterId;

        private Builder() {
        }

        public Builder screenId(Long screenId) {
            this.screenId = screenId;
            return this;
        }

        public Builder screenNumber(Integer screenNumber) {
            this.screenNumber = screenNumber;
            return this;
        }

        public Builder screenType(String screenType) {
            this.screenType = screenType;
            return this;
        }

        public Builder totalSeats(Integer totalSeats) {
            this.totalSeats = totalSeats;
            return this;
        }

        public Builder theaterId(Long theaterId) {
            this.theaterId = theaterId;
            return this;
        }

        public ScreenInfo build() {
            if (screenId == null || screenNumber == null || theaterId == null) {
                throw new IllegalStateException("Screen ID, screen number, and theater ID are required");
            }
            return new ScreenInfo(this);
        }
    }
}
