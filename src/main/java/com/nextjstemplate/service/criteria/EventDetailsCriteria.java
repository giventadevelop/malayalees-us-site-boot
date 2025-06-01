package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventDetails} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter caption;

    private StringFilter description;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter startTime;

    private StringFilter endTime;

    private StringFilter location;

    private StringFilter directionsToVenue;

    private IntegerFilter capacity;

    private StringFilter admissionType;

    private BooleanFilter isActive;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter createdById;

    private LongFilter eventTypeId;

    private Boolean distinct;

    public EventDetailsCriteria() {}

    public EventDetailsCriteria(EventDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.caption = other.caption == null ? null : other.caption.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.directionsToVenue = other.directionsToVenue == null ? null : other.directionsToVenue.copy();
        this.capacity = other.capacity == null ? null : other.capacity.copy();
        this.admissionType = other.admissionType == null ? null : other.admissionType.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.eventTypeId = other.eventTypeId == null ? null : other.eventTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventDetailsCriteria copy() {
        return new EventDetailsCriteria(this);
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

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getCaption() {
        return caption;
    }

    public StringFilter caption() {
        if (caption == null) {
            caption = new StringFilter();
        }
        return caption;
    }

    public void setCaption(StringFilter caption) {
        this.caption = caption;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getStartTime() {
        return startTime;
    }

    public StringFilter startTime() {
        if (startTime == null) {
            startTime = new StringFilter();
        }
        return startTime;
    }

    public void setStartTime(StringFilter startTime) {
        this.startTime = startTime;
    }

    public StringFilter getEndTime() {
        return endTime;
    }

    public StringFilter endTime() {
        if (endTime == null) {
            endTime = new StringFilter();
        }
        return endTime;
    }

    public void setEndTime(StringFilter endTime) {
        this.endTime = endTime;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getDirectionsToVenue() {
        return directionsToVenue;
    }

    public StringFilter directionsToVenue() {
        if (directionsToVenue == null) {
            directionsToVenue = new StringFilter();
        }
        return directionsToVenue;
    }

    public void setDirectionsToVenue(StringFilter directionsToVenue) {
        this.directionsToVenue = directionsToVenue;
    }

    public IntegerFilter getCapacity() {
        return capacity;
    }

    public IntegerFilter capacity() {
        if (capacity == null) {
            capacity = new IntegerFilter();
        }
        return capacity;
    }

    public void setCapacity(IntegerFilter capacity) {
        this.capacity = capacity;
    }

    public StringFilter getAdmissionType() {
        return admissionType;
    }

    public StringFilter admissionType() {
        if (admissionType == null) {
            admissionType = new StringFilter();
        }
        return admissionType;
    }

    public void setAdmissionType(StringFilter admissionType) {
        this.admissionType = admissionType;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
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

    public LongFilter getEventTypeId() {
        return eventTypeId;
    }

    public LongFilter eventTypeId() {
        if (eventTypeId == null) {
            eventTypeId = new LongFilter();
        }
        return eventTypeId;
    }

    public void setEventTypeId(LongFilter eventTypeId) {
        this.eventTypeId = eventTypeId;
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
        final EventDetailsCriteria that = (EventDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(caption, that.caption) &&
            Objects.equals(description, that.description) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(location, that.location) &&
            Objects.equals(directionsToVenue, that.directionsToVenue) &&
            Objects.equals(capacity, that.capacity) &&
            Objects.equals(admissionType, that.admissionType) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(eventTypeId, that.eventTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            title,
            caption,
            description,
            startDate,
            endDate,
            startTime,
            endTime,
            location,
            directionsToVenue,
            capacity,
            admissionType,
            isActive,
            createdAt,
            updatedAt,
            createdById,
            eventTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (caption != null ? "caption=" + caption + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (directionsToVenue != null ? "directionsToVenue=" + directionsToVenue + ", " : "") +
            (capacity != null ? "capacity=" + capacity + ", " : "") +
            (admissionType != null ? "admissionType=" + admissionType + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (eventTypeId != null ? "eventTypeId=" + eventTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
