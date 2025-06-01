package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDetailsDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String caption;

    @Size(max = 255)
    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Size(max = 100)
    private String startTime;

    @NotNull
    @Size(max = 100)
    private String endTime;

    @Size(max = 255)
    private String location;

    @Size(max = 255)
    private String directionsToVenue;

    private Integer capacity;

    @NotNull
    @Size(max = 255)
    private String admissionType;

    private Boolean isActive;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private UserProfileDTO createdBy;

    private EventTypeDetailsDTO eventType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDirectionsToVenue() {
        return directionsToVenue;
    }

    public void setDirectionsToVenue(String directionsToVenue) {
        this.directionsToVenue = directionsToVenue;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAdmissionType() {
        return admissionType;
    }

    public void setAdmissionType(String admissionType) {
        this.admissionType = admissionType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserProfileDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfileDTO createdBy) {
        this.createdBy = createdBy;
    }

    public EventTypeDetailsDTO getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeDetailsDTO eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDetailsDTO)) {
            return false;
        }

        EventDetailsDTO eventDetailsDTO = (EventDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDetailsDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", title='" + getTitle() + "'" +
            ", caption='" + getCaption() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", location='" + getLocation() + "'" +
            ", directionsToVenue='" + getDirectionsToVenue() + "'" +
            ", capacity=" + getCapacity() +
            ", admissionType='" + getAdmissionType() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", eventType=" + getEventType() +
            "}";
    }
}
