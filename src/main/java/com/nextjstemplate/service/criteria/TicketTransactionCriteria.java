package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.TicketTransaction} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.TicketTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ticket-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketTransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter email;

    private StringFilter firstName;

    private StringFilter lastName;

    private IntegerFilter quantity;

    private BigDecimalFilter pricePerUnit;

    private BigDecimalFilter totalAmount;

    private StringFilter status;

    private InstantFilter purchaseDate;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter eventId;

    private LongFilter ticketTypeId;

    private LongFilter userId;

    private Boolean distinct;

    public TicketTransactionCriteria() {}

    public TicketTransactionCriteria(TicketTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.pricePerUnit = other.pricePerUnit == null ? null : other.pricePerUnit.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.purchaseDate = other.purchaseDate == null ? null : other.purchaseDate.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.ticketTypeId = other.ticketTypeId == null ? null : other.ticketTypeId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TicketTransactionCriteria copy() {
        return new TicketTransactionCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getPricePerUnit() {
        return pricePerUnit;
    }

    public BigDecimalFilter pricePerUnit() {
        if (pricePerUnit == null) {
            pricePerUnit = new BigDecimalFilter();
        }
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimalFilter pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            totalAmount = new BigDecimalFilter();
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public InstantFilter getPurchaseDate() {
        return purchaseDate;
    }

    public InstantFilter purchaseDate() {
        if (purchaseDate == null) {
            purchaseDate = new InstantFilter();
        }
        return purchaseDate;
    }

    public void setPurchaseDate(InstantFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public LongFilter getTicketTypeId() {
        return ticketTypeId;
    }

    public LongFilter ticketTypeId() {
        if (ticketTypeId == null) {
            ticketTypeId = new LongFilter();
        }
        return ticketTypeId;
    }

    public void setTicketTypeId(LongFilter ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final TicketTransactionCriteria that = (TicketTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(email, that.email) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(pricePerUnit, that.pricePerUnit) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(ticketTypeId, that.ticketTypeId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            email,
            firstName,
            lastName,
            quantity,
            pricePerUnit,
            totalAmount,
            status,
            purchaseDate,
            createdAt,
            updatedAt,
            eventId,
            ticketTypeId,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (pricePerUnit != null ? "pricePerUnit=" + pricePerUnit + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (purchaseDate != null ? "purchaseDate=" + purchaseDate + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (ticketTypeId != null ? "ticketTypeId=" + ticketTypeId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
