package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CalendarEvent.
 */
@Entity
@Table(name = "calendar_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CalendarEvent implements Serializable {

    private static final long serialVersionUID = 1L;

//    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "calendar_provider", nullable = false)
    private String calendarProvider;

    @Column(name = "external_event_id")
    private String externalEventId;

    @NotNull
    @Column(name = "calendar_link", nullable = false)
    private String calendarLink;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userSubscription" }, allowSetters = true)
    private UserProfile createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CalendarEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalendarProvider() {
        return this.calendarProvider;
    }

    public CalendarEvent calendarProvider(String calendarProvider) {
        this.setCalendarProvider(calendarProvider);
        return this;
    }

    public void setCalendarProvider(String calendarProvider) {
        this.calendarProvider = calendarProvider;
    }

    public String getExternalEventId() {
        return this.externalEventId;
    }

    public CalendarEvent externalEventId(String externalEventId) {
        this.setExternalEventId(externalEventId);
        return this;
    }

    public void setExternalEventId(String externalEventId) {
        this.externalEventId = externalEventId;
    }

    public String getCalendarLink() {
        return this.calendarLink;
    }

    public CalendarEvent calendarLink(String calendarLink) {
        this.setCalendarLink(calendarLink);
        return this;
    }

    public void setCalendarLink(String calendarLink) {
        this.calendarLink = calendarLink;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CalendarEvent createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public CalendarEvent updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public CalendarEvent event(Event event) {
        this.setEvent(event);
        return this;
    }

    public UserProfile getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UserProfile userProfile) {
        this.createdBy = userProfile;
    }

    public CalendarEvent createdBy(UserProfile userProfile) {
        this.setCreatedBy(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalendarEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((CalendarEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalendarEvent{" +
            "id=" + getId() +
            ", calendarProvider='" + getCalendarProvider() + "'" +
            ", externalEventId='" + getExternalEventId() + "'" +
            ", calendarLink='" + getCalendarLink() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
