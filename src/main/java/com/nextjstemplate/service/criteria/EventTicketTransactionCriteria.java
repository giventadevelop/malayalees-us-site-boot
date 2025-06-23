package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventTicketTransaction} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventTicketTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-ticket-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter transactionReference;

    private StringFilter email;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter phone;

    private IntegerFilter quantity;

    private BigDecimalFilter pricePerUnit;

    private BigDecimalFilter totalAmount;

    private BigDecimalFilter taxAmount;

    private BigDecimalFilter feeAmount;

    private LongFilter discountCodeId;

    private BigDecimalFilter discountAmount;

    private BigDecimalFilter finalAmount;

    private StringFilter status;

    private StringFilter paymentMethod;

    private StringFilter paymentReference;

    private ZonedDateTimeFilter purchaseDate;

    private ZonedDateTimeFilter confirmationSentAt;

    private BigDecimalFilter refundAmount;

    private ZonedDateTimeFilter refundDate;

    private StringFilter stripeCheckoutSessionId;

    private StringFilter stripePaymentIntentId;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter ticketTypeId;

    private LongFilter userId;

    private Boolean distinct;

    public EventTicketTransactionCriteria() {}

    public EventTicketTransactionCriteria(EventTicketTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.transactionReference = other.transactionReference == null ? null : other.transactionReference.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.pricePerUnit = other.pricePerUnit == null ? null : other.pricePerUnit.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.taxAmount = other.taxAmount == null ? null : other.taxAmount.copy();
        this.feeAmount = other.feeAmount == null ? null : other.feeAmount.copy();
        this.discountCodeId = other.discountCodeId == null ? null : other.discountCodeId.copy();
        this.discountAmount = other.discountAmount == null ? null : other.discountAmount.copy();
        this.finalAmount = other.finalAmount == null ? null : other.finalAmount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.paymentMethod = other.paymentMethod == null ? null : other.paymentMethod.copy();
        this.paymentReference = other.paymentReference == null ? null : other.paymentReference.copy();
        this.purchaseDate = other.purchaseDate == null ? null : other.purchaseDate.copy();
        this.confirmationSentAt = other.confirmationSentAt == null ? null : other.confirmationSentAt.copy();
        this.refundAmount = other.refundAmount == null ? null : other.refundAmount.copy();
        this.refundDate = other.refundDate == null ? null : other.refundDate.copy();
        this.stripeCheckoutSessionId = other.stripeCheckoutSessionId == null ? null : other.stripeCheckoutSessionId.copy();
        this.stripePaymentIntentId = other.stripePaymentIntentId == null ? null : other.stripePaymentIntentId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.ticketTypeId = other.ticketTypeId == null ? null : other.ticketTypeId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventTicketTransactionCriteria copy() {
        return new EventTicketTransactionCriteria(this);
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

    public StringFilter getTransactionReference() {
        return transactionReference;
    }

    public StringFilter transactionReference() {
        if (transactionReference == null) {
            transactionReference = new StringFilter();
        }
        return transactionReference;
    }

    public void setTransactionReference(StringFilter transactionReference) {
        this.transactionReference = transactionReference;
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

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
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

    public BigDecimalFilter getTaxAmount() {
        return taxAmount;
    }

    public BigDecimalFilter taxAmount() {
        if (taxAmount == null) {
            taxAmount = new BigDecimalFilter();
        }
        return taxAmount;
    }

    public void setTaxAmount(BigDecimalFilter taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimalFilter getFeeAmount() {
        return feeAmount;
    }

    public BigDecimalFilter feeAmount() {
        if (feeAmount == null) {
            feeAmount = new BigDecimalFilter();
        }
        return feeAmount;
    }

    public void setFeeAmount(BigDecimalFilter feeAmount) {
        this.feeAmount = feeAmount;
    }

    public LongFilter getDiscountCodeId() {
        return discountCodeId;
    }

    public LongFilter discountCodeId() {
        if (discountCodeId == null) {
            discountCodeId = new LongFilter();
        }
        return discountCodeId;
    }

    public void setDiscountCodeId(LongFilter discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public BigDecimalFilter getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimalFilter discountAmount() {
        if (discountAmount == null) {
            discountAmount = new BigDecimalFilter();
        }
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimalFilter discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimalFilter getFinalAmount() {
        return finalAmount;
    }

    public BigDecimalFilter finalAmount() {
        if (finalAmount == null) {
            finalAmount = new BigDecimalFilter();
        }
        return finalAmount;
    }

    public void setFinalAmount(BigDecimalFilter finalAmount) {
        this.finalAmount = finalAmount;
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

    public StringFilter getPaymentMethod() {
        return paymentMethod;
    }

    public StringFilter paymentMethod() {
        if (paymentMethod == null) {
            paymentMethod = new StringFilter();
        }
        return paymentMethod;
    }

    public void setPaymentMethod(StringFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public StringFilter getPaymentReference() {
        return paymentReference;
    }

    public StringFilter paymentReference() {
        if (paymentReference == null) {
            paymentReference = new StringFilter();
        }
        return paymentReference;
    }

    public void setPaymentReference(StringFilter paymentReference) {
        this.paymentReference = paymentReference;
    }

    public ZonedDateTimeFilter getPurchaseDate() {
        return purchaseDate;
    }

    public ZonedDateTimeFilter purchaseDate() {
        if (purchaseDate == null) {
            purchaseDate = new ZonedDateTimeFilter();
        }
        return purchaseDate;
    }

    public void setPurchaseDate(ZonedDateTimeFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public ZonedDateTimeFilter getConfirmationSentAt() {
        return confirmationSentAt;
    }

    public ZonedDateTimeFilter confirmationSentAt() {
        if (confirmationSentAt == null) {
            confirmationSentAt = new ZonedDateTimeFilter();
        }
        return confirmationSentAt;
    }

    public void setConfirmationSentAt(ZonedDateTimeFilter confirmationSentAt) {
        this.confirmationSentAt = confirmationSentAt;
    }

    public BigDecimalFilter getRefundAmount() {
        return refundAmount;
    }

    public BigDecimalFilter refundAmount() {
        if (refundAmount == null) {
            refundAmount = new BigDecimalFilter();
        }
        return refundAmount;
    }

    public void setRefundAmount(BigDecimalFilter refundAmount) {
        this.refundAmount = refundAmount;
    }

    public ZonedDateTimeFilter getRefundDate() {
        return refundDate;
    }

    public ZonedDateTimeFilter refundDate() {
        if (refundDate == null) {
            refundDate = new ZonedDateTimeFilter();
        }
        return refundDate;
    }

    public void setRefundDate(ZonedDateTimeFilter refundDate) {
        this.refundDate = refundDate;
    }

    public StringFilter getStripeCheckoutSessionId() {
        return stripeCheckoutSessionId;
    }

    public StringFilter stripeCheckoutSessionId() {
        if (stripeCheckoutSessionId == null) {
            stripeCheckoutSessionId = new StringFilter();
        }
        return stripeCheckoutSessionId;
    }

    public void setStripeCheckoutSessionId(StringFilter stripeCheckoutSessionId) {
        this.stripeCheckoutSessionId = stripeCheckoutSessionId;
    }

    public StringFilter getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public StringFilter stripePaymentIntentId() {
        if (stripePaymentIntentId == null) {
            stripePaymentIntentId = new StringFilter();
        }
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(StringFilter stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
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
        final EventTicketTransactionCriteria that = (EventTicketTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(transactionReference, that.transactionReference) &&
            Objects.equals(email, that.email) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(pricePerUnit, that.pricePerUnit) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(taxAmount, that.taxAmount) &&
            Objects.equals(feeAmount, that.feeAmount) &&
            Objects.equals(discountCodeId, that.discountCodeId) &&
            Objects.equals(discountAmount, that.discountAmount) &&
            Objects.equals(finalAmount, that.finalAmount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(paymentReference, that.paymentReference) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(confirmationSentAt, that.confirmationSentAt) &&
            Objects.equals(refundAmount, that.refundAmount) &&
            Objects.equals(refundDate, that.refundDate) &&
            Objects.equals(stripeCheckoutSessionId, that.stripeCheckoutSessionId) &&
            Objects.equals(stripePaymentIntentId, that.stripePaymentIntentId) &&
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
            tenantId,
            transactionReference,
            email,
            firstName,
            lastName,
            phone,
            quantity,
            pricePerUnit,
            totalAmount,
            taxAmount,
            feeAmount,
            discountCodeId,
            discountAmount,
            finalAmount,
            status,
            paymentMethod,
            paymentReference,
            purchaseDate,
            confirmationSentAt,
            refundAmount,
            refundDate,
            stripeCheckoutSessionId,
            stripePaymentIntentId,
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
        return "EventTicketTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (transactionReference != null ? "transactionReference=" + transactionReference + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (pricePerUnit != null ? "pricePerUnit=" + pricePerUnit + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (taxAmount != null ? "taxAmount=" + taxAmount + ", " : "") +
            (feeAmount != null ? "feeAmount=" + feeAmount + ", " : "") +
            (discountCodeId != null ? "discountCodeId=" + discountCodeId + ", " : "") +
            (discountAmount != null ? "discountAmount=" + discountAmount + ", " : "") +
            (finalAmount != null ? "finalAmount=" + finalAmount + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (paymentMethod != null ? "paymentMethod=" + paymentMethod + ", " : "") +
            (paymentReference != null ? "paymentReference=" + paymentReference + ", " : "") +
            (purchaseDate != null ? "purchaseDate=" + purchaseDate + ", " : "") +
            (confirmationSentAt != null ? "confirmationSentAt=" + confirmationSentAt + ", " : "") +
            (refundAmount != null ? "refundAmount=" + refundAmount + ", " : "") +
            (refundDate != null ? "refundDate=" + refundDate + ", " : "") +
            (stripeCheckoutSessionId != null ? "stripeCheckoutSessionId=" + stripeCheckoutSessionId + ", " : "") +
            (stripePaymentIntentId != null ? "stripePaymentIntentId=" + stripePaymentIntentId + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (ticketTypeId != null ? "ticketTypeId=" + ticketTypeId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
