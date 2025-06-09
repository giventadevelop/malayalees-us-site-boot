package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventAttendeeGuest} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventAttendeeGuestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-attendee-guests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeGuestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter guestName;

    private StringFilter ageGroup;

    private StringFilter relationship;

    private StringFilter specialRequirements;

    private StringFilter registrationStatus;

    private StringFilter checkInStatus;

    private ZonedDateTimeFilter checkInTime;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter primaryAttendeeId;

    private Boolean distinct;

    public EventAttendeeGuestCriteria() {}

    public EventAttendeeGuestCriteria(EventAttendeeGuestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.guestName = other.guestName == null ? null : other.guestName.copy();
        this.ageGroup = other.ageGroup == null ? null : other.ageGroup.copy();
        this.relationship = other.relationship == null ? null : other.relationship.copy();
        this.specialRequirements = other.specialRequirements == null ? null : other.specialRequirements.copy();
        this.registrationStatus = other.registrationStatus == null ? null : other.registrationStatus.copy();
        this.checkInStatus = other.checkInStatus == null ? null : other.checkInStatus.copy();
        this.checkInTime = other.checkInTime == null ? null : other.checkInTime.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.primaryAttendeeId = other.primaryAttendeeId == null ? null : other.primaryAttendeeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventAttendeeGuestCriteria copy() {
        return new EventAttendeeGuestCriteria(this);
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

    public StringFilter getGuestName() {
        return guestName;
    }

    public StringFilter guestName() {
        if (guestName == null) {
            guestName = new StringFilter();
        }
        return guestName;
    }

    public void setGuestName(StringFilter guestName) {
        this.guestName = guestName;
    }

    public StringFilter getAgeGroup() {
        return ageGroup;
    }

    public StringFilter ageGroup() {
        if (ageGroup == null) {
            ageGroup = new StringFilter();
        }
        return ageGroup;
    }

    public void setAgeGroup(StringFilter ageGroup) {
        this.ageGroup = ageGroup;
    }

    public StringFilter getRelationship() {
        return relationship;
    }

    public StringFilter relationship() {
        if (relationship == null) {
            relationship = new StringFilter();
        }
        return relationship;
    }

    public void setRelationship(StringFilter relationship) {
        this.relationship = relationship;
    }

    public StringFilter getSpecialRequirements() {
        return specialRequirements;
    }

    public StringFilter specialRequirements() {
        if (specialRequirements == null) {
            specialRequirements = new StringFilter();
        }
        return specialRequirements;
    }

    public void setSpecialRequirements(StringFilter specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public StringFilter getRegistrationStatus() {
        return registrationStatus;
    }

    public StringFilter registrationStatus() {
        if (registrationStatus == null) {
            registrationStatus = new StringFilter();
        }
        return registrationStatus;
    }

    public void setRegistrationStatus(StringFilter registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public StringFilter getCheckInStatus() {
        return checkInStatus;
    }

    public StringFilter checkInStatus() {
        if (checkInStatus == null) {
            checkInStatus = new StringFilter();
        }
        return checkInStatus;
    }

    public void setCheckInStatus(StringFilter checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public ZonedDateTimeFilter getCheckInTime() {
        return checkInTime;
    }

    public ZonedDateTimeFilter checkInTime() {
        if (checkInTime == null) {
            checkInTime = new ZonedDateTimeFilter();
        }
        return checkInTime;
    }

    public void setCheckInTime(ZonedDateTimeFilter checkInTime) {
        this.checkInTime = checkInTime;
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

    public LongFilter getPrimaryAttendeeId() {
        return primaryAttendeeId;
    }

    public LongFilter primaryAttendeeId() {
        if (primaryAttendeeId == null) {
            primaryAttendeeId = new LongFilter();
        }
        return primaryAttendeeId;
    }

    public void setPrimaryAttendeeId(LongFilter primaryAttendeeId) {
        this.primaryAttendeeId = primaryAttendeeId;
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
        final EventAttendeeGuestCriteria that = (EventAttendeeGuestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(guestName, that.guestName) &&
            Objects.equals(ageGroup, that.ageGroup) &&
            Objects.equals(relationship, that.relationship) &&
            Objects.equals(specialRequirements, that.specialRequirements) &&
            Objects.equals(registrationStatus, that.registrationStatus) &&
            Objects.equals(checkInStatus, that.checkInStatus) &&
            Objects.equals(checkInTime, that.checkInTime) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(primaryAttendeeId, that.primaryAttendeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            guestName,
            ageGroup,
            relationship,
            specialRequirements,
            registrationStatus,
            checkInStatus,
            checkInTime,
            createdAt,
            updatedAt,
            primaryAttendeeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendeeGuestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (guestName != null ? "guestName=" + guestName + ", " : "") +
            (ageGroup != null ? "ageGroup=" + ageGroup + ", " : "") +
            (relationship != null ? "relationship=" + relationship + ", " : "") +
            (specialRequirements != null ? "specialRequirements=" + specialRequirements + ", " : "") +
            (registrationStatus != null ? "registrationStatus=" + registrationStatus + ", " : "") +
            (checkInStatus != null ? "checkInStatus=" + checkInStatus + ", " : "") +
            (checkInTime != null ? "checkInTime=" + checkInTime + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (primaryAttendeeId != null ? "primaryAttendeeId=" + primaryAttendeeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
