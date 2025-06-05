package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventAttendee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    private String registrationStatus;

    @NotNull
    private ZonedDateTime registrationDate;

    private ZonedDateTime confirmationDate;

    private ZonedDateTime cancellationDate;

    @Size(max = 500)
    private String cancellationReason;

    @Size(max = 50)
    private String attendeeType;

    @Size(max = 1000)
    private String specialRequirements;

    @Size(max = 255)
    private String emergencyContactName;

    @Size(max = 50)
    private String emergencyContactPhone;

    @Size(max = 50)
    private String checkInStatus;

    private ZonedDateTime checkInTime;

    @Size(max = 1000)
    private String notes;

    @Size(max = 1000)
    private String qrCodeData;

    private Boolean qrCodeGenerated;

    private ZonedDateTime qrCodeGeneratedAt;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private UserProfileDTO attendee;

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

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public ZonedDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(ZonedDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ZonedDateTime getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(ZonedDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public ZonedDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(ZonedDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getAttendeeType() {
        return attendeeType;
    }

    public void setAttendeeType(String attendeeType) {
        this.attendeeType = attendeeType;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public ZonedDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(ZonedDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public Boolean getQrCodeGenerated() {
        return qrCodeGenerated;
    }

    public void setQrCodeGenerated(Boolean qrCodeGenerated) {
        this.qrCodeGenerated = qrCodeGenerated;
    }

    public ZonedDateTime getQrCodeGeneratedAt() {
        return qrCodeGeneratedAt;
    }

    public void setQrCodeGeneratedAt(ZonedDateTime qrCodeGeneratedAt) {
        this.qrCodeGeneratedAt = qrCodeGeneratedAt;
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

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    public UserProfileDTO getAttendee() {
        return attendee;
    }

    public void setAttendee(UserProfileDTO attendee) {
        this.attendee = attendee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAttendeeDTO)) {
            return false;
        }

        EventAttendeeDTO eventAttendeeDTO = (EventAttendeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventAttendeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendeeDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", registrationStatus='" + getRegistrationStatus() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", confirmationDate='" + getConfirmationDate() + "'" +
            ", cancellationDate='" + getCancellationDate() + "'" +
            ", cancellationReason='" + getCancellationReason() + "'" +
            ", attendeeType='" + getAttendeeType() + "'" +
            ", specialRequirements='" + getSpecialRequirements() + "'" +
            ", emergencyContactName='" + getEmergencyContactName() + "'" +
            ", emergencyContactPhone='" + getEmergencyContactPhone() + "'" +
            ", checkInStatus='" + getCheckInStatus() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", notes='" + getNotes() + "'" +
            ", qrCodeData='" + getQrCodeData() + "'" +
            ", qrCodeGenerated='" + getQrCodeGenerated() + "'" +
            ", qrCodeGeneratedAt='" + getQrCodeGeneratedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", attendee=" + getAttendee() +
            "}";
    }
}
