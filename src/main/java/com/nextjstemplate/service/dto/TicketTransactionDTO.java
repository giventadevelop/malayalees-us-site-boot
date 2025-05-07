package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.TicketTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketTransactionDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String email;

    private String firstName;

    private String lastName;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private String status;

    @NotNull
    private Instant purchaseDate;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private EventDTO event;

    private TicketTypeDTO ticketType;

    private UserProfileDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Instant purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public TicketTypeDTO getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketTypeDTO ticketType) {
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
        if (!(o instanceof TicketTransactionDTO)) {
            return false;
        }

        TicketTransactionDTO ticketTransactionDTO = (TicketTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketTransactionDTO{" +
            "id=" + getId() +
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
