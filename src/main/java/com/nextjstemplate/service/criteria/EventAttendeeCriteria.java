package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventAttendee} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventAttendeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-attendees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter registrationStatus;

    private ZonedDateTimeFilter registrationDate;

    private ZonedDateTimeFilter confirmationDate;

    private ZonedDateTimeFilter cancellationDate;

    private StringFilter cancellationReason;

    private StringFilter attendeeType;

    private StringFilter specialRequirements;

    private StringFilter emergencyContactName;

    private StringFilter emergencyContactPhone;

    private StringFilter checkInStatus;

    private ZonedDateTimeFilter checkInTime;

    private StringFilter notes;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter attendeeId;

    private Boolean distinct;

    public EventAttendeeCriteria() {}

    public EventAttendeeCriteria(EventAttendeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.registrationStatus = other.registrationStatus == null ? null : other.registrationStatus.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.confirmationDate = other.confirmationDate == null ? null : other.confirmationDate.copy();
        this.cancellationDate = other.cancellationDate == null ? null : other.cancellationDate.copy();
        this.cancellationReason = other.cancellationReason == null ? null : other.cancellationReason.copy();
        this.attendeeType = other.attendeeType == null ? null : other.attendeeType.copy();
        this.specialRequirements = other.specialRequirements == null ? null : other.specialRequirements.copy();
        this.emergencyContactName = other.emergencyContactName == null ? null : other.emergencyContactName.copy();
        this.emergencyContactPhone = other.emergencyContactPhone == null ? null : other.emergencyContactPhone.copy();
        this.checkInStatus = other.checkInStatus == null ? null : other.checkInStatus.copy();
        this.checkInTime = other.checkInTime == null ? null : other.checkInTime.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.attendeeId = other.attendeeId == null ? null : other.attendeeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventAttendeeCriteria copy() {
        return new EventAttendeeCriteria(this);
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

    public ZonedDateTimeFilter getRegistrationDate() {
        return registrationDate;
    }

    public ZonedDateTimeFilter registrationDate() {
        if (registrationDate == null) {
            registrationDate = new ZonedDateTimeFilter();
        }
        return registrationDate;
    }

    public void setRegistrationDate(ZonedDateTimeFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ZonedDateTimeFilter getConfirmationDate() {
        return confirmationDate;
    }

    public ZonedDateTimeFilter confirmationDate() {
        if (confirmationDate == null) {
            confirmationDate = new ZonedDateTimeFilter();
        }
        return confirmationDate;
    }

    public void setConfirmationDate(ZonedDateTimeFilter confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public ZonedDateTimeFilter getCancellationDate() {
        return cancellationDate;
    }

    public ZonedDateTimeFilter cancellationDate() {
        if (cancellationDate == null) {
            cancellationDate = new ZonedDateTimeFilter();
        }
        return cancellationDate;
    }

    public void setCancellationDate(ZonedDateTimeFilter cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public StringFilter getCancellationReason() {
        return cancellationReason;
    }

    public StringFilter cancellationReason() {
        if (cancellationReason == null) {
            cancellationReason = new StringFilter();
        }
        return cancellationReason;
    }

    public void setCancellationReason(StringFilter cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public StringFilter getAttendeeType() {
        return attendeeType;
    }

    public StringFilter attendeeType() {
        if (attendeeType == null) {
            attendeeType = new StringFilter();
        }
        return attendeeType;
    }

    public void setAttendeeType(StringFilter attendeeType) {
        this.attendeeType = attendeeType;
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

    public StringFilter getEmergencyContactName() {
        return emergencyContactName;
    }

    public StringFilter emergencyContactName() {
        if (emergencyContactName == null) {
            emergencyContactName = new StringFilter();
        }
        return emergencyContactName;
    }

    public void setEmergencyContactName(StringFilter emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public StringFilter getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public StringFilter emergencyContactPhone() {
        if (emergencyContactPhone == null) {
            emergencyContactPhone = new StringFilter();
        }
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(StringFilter emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
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

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
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

    public LongFilter getAttendeeId() {
        return attendeeId;
    }

    public LongFilter attendeeId() {
        if (attendeeId == null) {
            attendeeId = new LongFilter();
        }
        return attendeeId;
    }

    public void setAttendeeId(LongFilter attendeeId) {
        this.attendeeId = attendeeId;
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
        final EventAttendeeCriteria that = (EventAttendeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(registrationStatus, that.registrationStatus) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(confirmationDate, that.confirmationDate) &&
            Objects.equals(cancellationDate, that.cancellationDate) &&
            Objects.equals(cancellationReason, that.cancellationReason) &&
            Objects.equals(attendeeType, that.attendeeType) &&
            Objects.equals(specialRequirements, that.specialRequirements) &&
            Objects.equals(emergencyContactName, that.emergencyContactName) &&
            Objects.equals(emergencyContactPhone, that.emergencyContactPhone) &&
            Objects.equals(checkInStatus, that.checkInStatus) &&
            Objects.equals(checkInTime, that.checkInTime) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(attendeeId, that.attendeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            registrationStatus,
            registrationDate,
            confirmationDate,
            cancellationDate,
            cancellationReason,
            attendeeType,
            specialRequirements,
            emergencyContactName,
            emergencyContactPhone,
            checkInStatus,
            checkInTime,
            notes,
            createdAt,
            updatedAt,
            eventId,
            attendeeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (registrationStatus != null ? "registrationStatus=" + registrationStatus + ", " : "") +
            (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
            (confirmationDate != null ? "confirmationDate=" + confirmationDate + ", " : "") +
            (cancellationDate != null ? "cancellationDate=" + cancellationDate + ", " : "") +
            (cancellationReason != null ? "cancellationReason=" + cancellationReason + ", " : "") +
            (attendeeType != null ? "attendeeType=" + attendeeType + ", " : "") +
            (specialRequirements != null ? "specialRequirements=" + specialRequirements + ", " : "") +
            (emergencyContactName != null ? "emergencyContactName=" + emergencyContactName + ", " : "") +
            (emergencyContactPhone != null ? "emergencyContactPhone=" + emergencyContactPhone + ", " : "") +
            (checkInStatus != null ? "checkInStatus=" + checkInStatus + ", " : "") +
            (checkInTime != null ? "checkInTime=" + checkInTime + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (attendeeId != null ? "attendeeId=" + attendeeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
