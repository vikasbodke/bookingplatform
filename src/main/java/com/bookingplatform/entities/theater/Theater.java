package com.bookingplatform.entities.theater;

import com.bookingplatform.entities.city.City;
import com.bookingplatform.entities.theaterpartner.TheaterPartner;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("theaters")
public class Theater {
    
    @Id
    @Column("theater_id")
    private Long theaterId;
    
    @Column("name")
    private String name;
    
    @Column("location")
    private String location;
    
    @Column("city_id")
    private Long cityId;
    private transient City city;
    
    @Column("partner_id")
    private Long partnerId;
    private transient TheaterPartner partner;
    
    @Column("number_of_screens")
    private Integer numberOfScreens;
    
    @Column("contact_phone")
    private String contactPhone;
    
    @Column("is_active")
    private Boolean isActive;
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public Theater() {
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
        this.updatedAt = LocalDateTime.now();
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
        if (city != null) {
            this.cityId = city.getCityId();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
        this.updatedAt = LocalDateTime.now();
    }

    public TheaterPartner getPartner() {
        return partner;
    }

    public void setPartner(TheaterPartner partner) {
        this.partner = partner;
        if (partner != null) {
            this.partnerId = partner.getPartnerId();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getNumberOfScreens() {
        return numberOfScreens;
    }

    public void setNumberOfScreens(Integer numberOfScreens) {
        this.numberOfScreens = numberOfScreens;
        this.updatedAt = LocalDateTime.now();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateTimestamps() {
        this.updatedAt = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = this.updatedAt;
        }
    }
}
