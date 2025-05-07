package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.CalendarEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CalendarEventDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String calendarProvider;

    private String externalEventId;

    @NotNull
    private String calendarLink;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private EventDTO event;

    private UserProfileDTO createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalendarProvider() {
        return calendarProvider;
    }

    public void setCalendarProvider(String calendarProvider) {
        this.calendarProvider = calendarProvider;
    }

    public String getExternalEventId() {
        return externalEventId;
    }

    public void setExternalEventId(String externalEventId) {
        this.externalEventId = externalEventId;
    }

    public String getCalendarLink() {
        return calendarLink;
    }

    public void setCalendarLink(String calendarLink) {
        this.calendarLink = calendarLink;
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

    public UserProfileDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfileDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalendarEventDTO)) {
            return false;
        }

        CalendarEventDTO calendarEventDTO = (CalendarEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, calendarEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalendarEventDTO{" +
            "id=" + getId() +
            ", calendarProvider='" + getCalendarProvider() + "'" +
            ", externalEventId='" + getExternalEventId() + "'" +
            ", calendarLink='" + getCalendarLink() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
