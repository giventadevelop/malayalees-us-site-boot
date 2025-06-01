package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventAttendee.
 */
@Entity
@Table(name = "event_attendee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    @Column(name = "registration_status", length = 50, nullable = false)
    private String registrationStatus;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private ZonedDateTime registrationDate;

    @Column(name = "confirmation_date")
    private ZonedDateTime confirmationDate;

    @Column(name = "cancellation_date")
    private ZonedDateTime cancellationDate;

    @Size(max = 500)
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Size(max = 50)
    @Column(name = "attendee_type", length = 50)
    private String attendeeType;

    @Size(max = 1000)
    @Column(name = "special_requirements", length = 1000)
    private String specialRequirements;

    @Size(max = 255)
    @Column(name = "emergency_contact_name", length = 255)
    private String emergencyContactName;

    @Size(max = 50)
    @Column(name = "emergency_contact_phone", length = 50)
    private String emergencyContactPhone;

    @Size(max = 50)
    @Column(name = "check_in_status", length = 50)
    private String checkInStatus;

    @Column(name = "check_in_time")
    private ZonedDateTime checkInTime;

    @Size(max = 1000)
    @Column(name = "notes", length = 1000)
    private String notes;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userSubscription" }, allowSetters = true)
    private UserProfile attendee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventAttendee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventAttendee tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRegistrationStatus() {
        return this.registrationStatus;
    }

    public EventAttendee registrationStatus(String registrationStatus) {
        this.setRegistrationStatus(registrationStatus);
        return this;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public ZonedDateTime getRegistrationDate() {
        return this.registrationDate;
    }

    public EventAttendee registrationDate(ZonedDateTime registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(ZonedDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ZonedDateTime getConfirmationDate() {
        return this.confirmationDate;
    }

    public EventAttendee confirmationDate(ZonedDateTime confirmationDate) {
        this.setConfirmationDate(confirmationDate);
        return this;
    }

    public void setConfirmationDate(ZonedDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public ZonedDateTime getCancellationDate() {
        return this.cancellationDate;
    }

    public EventAttendee cancellationDate(ZonedDateTime cancellationDate) {
        this.setCancellationDate(cancellationDate);
        return this;
    }

    public void setCancellationDate(ZonedDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public EventAttendee cancellationReason(String cancellationReason) {
        this.setCancellationReason(cancellationReason);
        return this;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getAttendeeType() {
        return this.attendeeType;
    }

    public EventAttendee attendeeType(String attendeeType) {
        this.setAttendeeType(attendeeType);
        return this;
    }

    public void setAttendeeType(String attendeeType) {
        this.attendeeType = attendeeType;
    }

    public String getSpecialRequirements() {
        return this.specialRequirements;
    }

    public EventAttendee specialRequirements(String specialRequirements) {
        this.setSpecialRequirements(specialRequirements);
        return this;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getEmergencyContactName() {
        return this.emergencyContactName;
    }

    public EventAttendee emergencyContactName(String emergencyContactName) {
        this.setEmergencyContactName(emergencyContactName);
        return this;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return this.emergencyContactPhone;
    }

    public EventAttendee emergencyContactPhone(String emergencyContactPhone) {
        this.setEmergencyContactPhone(emergencyContactPhone);
        return this;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getCheckInStatus() {
        return this.checkInStatus;
    }

    public EventAttendee checkInStatus(String checkInStatus) {
        this.setCheckInStatus(checkInStatus);
        return this;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public ZonedDateTime getCheckInTime() {
        return this.checkInTime;
    }

    public EventAttendee checkInTime(ZonedDateTime checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public void setCheckInTime(ZonedDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getNotes() {
        return this.notes;
    }

    public EventAttendee notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventAttendee createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventAttendee updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public EventAttendee event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    public UserProfile getAttendee() {
        return this.attendee;
    }

    public void setAttendee(UserProfile userProfile) {
        this.attendee = userProfile;
    }

    public EventAttendee attendee(UserProfile userProfile) {
        this.setAttendee(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAttendee)) {
            return false;
        }
        return getId() != null && getId().equals(((EventAttendee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendee{" +
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
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
