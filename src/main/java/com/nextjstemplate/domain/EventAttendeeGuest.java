package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Event Guest Management JDL Model
 * Enhanced entities for sophisticated guest management with age-based pricing
 */
@Entity
@Table(name = "event_attendee_guest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeGuest implements Serializable {

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
    @Size(max = 255)
    @Column(name = "guest_name", length = 255, nullable = false)
    private String guestName;

    @Size(max = 20)
    @Column(name = "age_group", length = 20)
    private String ageGroup;

    @Size(max = 50)
    @Column(name = "relationship", length = 50)
    private String relationship;

    @Size(max = 500)
    @Column(name = "special_requirements", length = 500)
    private String specialRequirements;

    @Size(max = 50)
    @Column(name = "registration_status", length = 50)
    private String registrationStatus;

    @Size(max = 50)
    @Column(name = "check_in_status", length = 50)
    private String checkInStatus;

    @Column(name = "check_in_time")
    private ZonedDateTime checkInTime;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "event", "attendee" }, allowSetters = true)
    private EventAttendee primaryAttendee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventAttendeeGuest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventAttendeeGuest tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getGuestName() {
        return this.guestName;
    }

    public EventAttendeeGuest guestName(String guestName) {
        this.setGuestName(guestName);
        return this;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getAgeGroup() {
        return this.ageGroup;
    }

    public EventAttendeeGuest ageGroup(String ageGroup) {
        this.setAgeGroup(ageGroup);
        return this;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getRelationship() {
        return this.relationship;
    }

    public EventAttendeeGuest relationship(String relationship) {
        this.setRelationship(relationship);
        return this;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getSpecialRequirements() {
        return this.specialRequirements;
    }

    public EventAttendeeGuest specialRequirements(String specialRequirements) {
        this.setSpecialRequirements(specialRequirements);
        return this;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getRegistrationStatus() {
        return this.registrationStatus;
    }

    public EventAttendeeGuest registrationStatus(String registrationStatus) {
        this.setRegistrationStatus(registrationStatus);
        return this;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getCheckInStatus() {
        return this.checkInStatus;
    }

    public EventAttendeeGuest checkInStatus(String checkInStatus) {
        this.setCheckInStatus(checkInStatus);
        return this;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public ZonedDateTime getCheckInTime() {
        return this.checkInTime;
    }

    public EventAttendeeGuest checkInTime(ZonedDateTime checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public void setCheckInTime(ZonedDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventAttendeeGuest createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventAttendeeGuest updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventAttendee getPrimaryAttendee() {
        return this.primaryAttendee;
    }

    public void setPrimaryAttendee(EventAttendee eventAttendee) {
        this.primaryAttendee = eventAttendee;
    }

    public EventAttendeeGuest primaryAttendee(EventAttendee eventAttendee) {
        this.setPrimaryAttendee(eventAttendee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAttendeeGuest)) {
            return false;
        }
        return getId() != null && getId().equals(((EventAttendeeGuest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendeeGuest{" +
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
            "}";
    }
}
