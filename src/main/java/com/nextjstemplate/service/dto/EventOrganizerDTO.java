package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventOrganizer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventOrganizerDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String title;

    private String designation;

    private String contactEmail;

    private String contactPhone;

    private Boolean isPrimary;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private EventDTO event;

    private UserProfileDTO organizer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public UserProfileDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserProfileDTO organizer) {
        this.organizer = organizer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventOrganizerDTO)) {
            return false;
        }

        EventOrganizerDTO eventOrganizerDTO = (EventOrganizerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventOrganizerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventOrganizerDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", contactPhone='" + getContactPhone() + "'" +
            ", isPrimary='" + getIsPrimary() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", organizer=" + getOrganizer() +
            "}";
    }
}
