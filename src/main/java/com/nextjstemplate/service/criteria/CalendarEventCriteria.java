package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.CalendarEvent} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.CalendarEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /calendar-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CalendarEventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter calendarProvider;

    private StringFilter externalEventId;

    private StringFilter calendarLink;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter eventId;

    private LongFilter createdById;

    private Boolean distinct;

    public CalendarEventCriteria() {}

    public CalendarEventCriteria(CalendarEventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.calendarProvider = other.calendarProvider == null ? null : other.calendarProvider.copy();
        this.externalEventId = other.externalEventId == null ? null : other.externalEventId.copy();
        this.calendarLink = other.calendarLink == null ? null : other.calendarLink.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CalendarEventCriteria copy() {
        return new CalendarEventCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCalendarProvider() {
        return calendarProvider;
    }

    public StringFilter calendarProvider() {
        if (calendarProvider == null) {
            calendarProvider = new StringFilter();
        }
        return calendarProvider;
    }

    public void setCalendarProvider(StringFilter calendarProvider) {
        this.calendarProvider = calendarProvider;
    }

    public StringFilter getExternalEventId() {
        return externalEventId;
    }

    public StringFilter externalEventId() {
        if (externalEventId == null) {
            externalEventId = new StringFilter();
        }
        return externalEventId;
    }

    public void setExternalEventId(StringFilter externalEventId) {
        this.externalEventId = externalEventId;
    }

    public StringFilter getCalendarLink() {
        return calendarLink;
    }

    public StringFilter calendarLink() {
        if (calendarLink == null) {
            calendarLink = new StringFilter();
        }
        return calendarLink;
    }

    public void setCalendarLink(StringFilter calendarLink) {
        this.calendarLink = calendarLink;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CalendarEventCriteria that = (CalendarEventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(calendarProvider, that.calendarProvider) &&
            Objects.equals(externalEventId, that.externalEventId) &&
            Objects.equals(calendarLink, that.calendarLink) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, calendarProvider, externalEventId, calendarLink, createdAt, updatedAt, eventId, createdById, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalendarEventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (calendarProvider != null ? "calendarProvider=" + calendarProvider + ", " : "") +
            (externalEventId != null ? "externalEventId=" + externalEventId + ", " : "") +
            (calendarLink != null ? "calendarLink=" + calendarLink + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
