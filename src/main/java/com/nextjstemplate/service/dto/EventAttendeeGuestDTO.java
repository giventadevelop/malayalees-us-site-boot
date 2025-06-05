package com.nextjstemplate.service.dto;

import com.nextjstemplate.domain.enumeration.GuestAgeGroup;
import com.nextjstemplate.domain.enumeration.UserEventCheckInStatus;
import com.nextjstemplate.domain.enumeration.UserEventRegistrationStatus;
import com.nextjstemplate.domain.enumeration.UserToGuestRelationship;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventAttendeeGuest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeGuestDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String guestName;

    @NotNull
    private GuestAgeGroup ageGroup;

    private UserToGuestRelationship relationship;

    @Size(max = 500)
    private String specialRequirements;

    private UserEventRegistrationStatus registrationStatus;

    private UserEventCheckInStatus checkInStatus;

    private ZonedDateTime checkInTime;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventAttendeeDTO primaryAttendee;

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

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public GuestAgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(GuestAgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public UserToGuestRelationship getRelationship() {
        return relationship;
    }

    public void setRelationship(UserToGuestRelationship relationship) {
        this.relationship = relationship;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public UserEventRegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(UserEventRegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public UserEventCheckInStatus getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(UserEventCheckInStatus checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public ZonedDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(ZonedDateTime checkInTime) {
        this.checkInTime = checkInTime;
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

    public EventAttendeeDTO getPrimaryAttendee() {
        return primaryAttendee;
    }

    public void setPrimaryAttendee(EventAttendeeDTO primaryAttendee) {
        this.primaryAttendee = primaryAttendee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAttendeeGuestDTO)) {
            return false;
        }

        EventAttendeeGuestDTO eventAttendeeGuestDTO = (EventAttendeeGuestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventAttendeeGuestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendeeGuestDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", guestName='" + getGuestName() + "'" +
            ", ageGroup='" + getAgeGroup() + "'" +
            ", relationship='" + getRelationship() + "'" +
            ", specialRequirements='" + getSpecialRequirements() + "'" +
            ", registrationStatus='" + getRegistrationStatus() + "'" +
            ", checkInStatus='" + getCheckInStatus() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", primaryAttendee=" + getPrimaryAttendee() +
            "}";
    }
}
