package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventTicketTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTransactionDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    @Size(max = 255)
    private String status;

    @NotNull
    private ZonedDateTime purchaseDate;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private EventTicketTypeDTO ticketType;

    private UserProfileDTO user;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(ZonedDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public EventTicketTypeDTO getTicketType() {
        return ticketType;
    }

    public void setTicketType(EventTicketTypeDTO ticketType) {
        this.ticketType = ticketType;
    }

    public UserProfileDTO getUser() {
        return user;
    }

    public void setUser(UserProfileDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTicketTransactionDTO)) {
            return false;
        }

        EventTicketTransactionDTO eventTicketTransactionDTO = (EventTicketTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventTicketTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTransactionDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", quantity=" + getQuantity() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", totalAmount=" + getTotalAmount() +
            ", status='" + getStatus() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", ticketType=" + getTicketType() +
            ", user=" + getUser() +
            "}";
    }
}
