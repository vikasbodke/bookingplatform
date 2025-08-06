package com.bookingplatform.usecases.browsing.dto;

public  class BrowseTheaterInfo {
    private Integer theaterId;
    private String name;
    private String location;

    public BrowseTheaterInfo() {
    }

    public BrowseTheaterInfo(Integer theaterId, String name, String location) {
        this.theaterId = theaterId;
        this.name = name;
        this.location = location;
    }

    // Getters and Setters
    public Integer getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Integer theaterId) {
        this.theaterId = theaterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer theaterId;
        private String name;
        private String location;

        public Builder theaterId(Integer theaterId) {
            this.theaterId = theaterId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public BrowseTheaterInfo build() {
            return new BrowseTheaterInfo(theaterId, name, location);
        }
    }
}
