package com.bookingplatform.usecases.browsing.dto;

import java.util.Objects;

public class TheaterInfo {
    private final Long theaterId;
    private final String name;
    private final String address;
    private final String city;
    private final String state;
    private final String pincode;
    private final Double latitude;
    private final Double longitude;

    private TheaterInfo(Builder builder) {
        this.theaterId = builder.theaterId;
        this.name = builder.name;
        this.address = builder.address;
        this.city = builder.city;
        this.state = builder.state;
        this.pincode = builder.pincode;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    // Getters
    public Long getTheaterId() {
        return theaterId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheaterInfo that = (TheaterInfo) o;
        return Objects.equals(theaterId, that.theaterId) &&
               Objects.equals(name, that.name) &&
               Objects.equals(address, that.address) &&
               Objects.equals(city, that.city) &&
               Objects.equals(state, that.state) &&
               Objects.equals(pincode, that.pincode) &&
               Objects.equals(latitude, that.latitude) &&
               Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theaterId, name, address, city, state, pincode, latitude, longitude);
    }

    @Override
    public String toString() {
        return "TheaterInfo{" +
               "theaterId=" + theaterId +
               ", name='" + name + '\'' +
               ", address='" + address + '\'' +
               ", city='" + city + '\'' +
               ", state='" + state + '\'' +
               ", pincode='" + pincode + '\'' +
               ", latitude=" + latitude +
               ", longitude=" + longitude +
               '}';
    }

    public static final class Builder {
        private Long theaterId;
        private String name;
        private String address;
        private String city;
        private String state;
        private String pincode;
        private Double latitude;
        private Double longitude;

        private Builder() {
        }

        public Builder theaterId(Long theaterId) {
            this.theaterId = theaterId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder pincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public Builder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public TheaterInfo build() {
            if (theaterId == null || name == null) {
                throw new IllegalStateException("Theater ID and name are required");
            }
            return new TheaterInfo(this);
        }
    }
}
