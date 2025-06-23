package com.nextjstemplate.service.dto;

import jakarta.persistence.Lob;
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

    @Size(max = 255)
    private String transactionReference;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    private String phone;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    @NotNull
    private BigDecimal totalAmount;

    private BigDecimal taxAmount;

    private BigDecimal feeAmount;

    private Long discountCodeId;

    private BigDecimal discountAmount;

    @NotNull
    private BigDecimal finalAmount;

    @NotNull
    @Size(max = 255)
    private String status;

    @Size(max = 100)
    private String paymentMethod;

    @Size(max = 255)
    private String paymentReference;

    @NotNull
    private ZonedDateTime purchaseDate;

    private ZonedDateTime confirmationSentAt;

    private BigDecimal refundAmount;

    private ZonedDateTime refundDate;

    @Lob
    private String refundReason;

    @Size(max = 255)
    private String stripeCheckoutSessionId;

    @Size(max = 255)
    private String stripePaymentIntentId;

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

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Long getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(Long discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public ZonedDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(ZonedDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public ZonedDateTime getConfirmationSentAt() {
        return confirmationSentAt;
    }

    public void setConfirmationSentAt(ZonedDateTime confirmationSentAt) {
        this.confirmationSentAt = confirmationSentAt;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public ZonedDateTime getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(ZonedDateTime refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getStripeCheckoutSessionId() {
        return stripeCheckoutSessionId;
    }

    public void setStripeCheckoutSessionId(String stripeCheckoutSessionId) {
        this.stripeCheckoutSessionId = stripeCheckoutSessionId;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
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
            ", transactionReference='" + getTransactionReference() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", quantity=" + getQuantity() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", totalAmount=" + getTotalAmount() +
            ", taxAmount=" + getTaxAmount() +
            ", feeAmount=" + getFeeAmount() +
            ", discountCodeId=" + getDiscountCodeId() +
            ", discountAmount=" + getDiscountAmount() +
            ", finalAmount=" + getFinalAmount() +
            ", status='" + getStatus() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", confirmationSentAt='" + getConfirmationSentAt() + "'" +
            ", refundAmount=" + getRefundAmount() +
            ", refundDate='" + getRefundDate() + "'" +
            ", refundReason='" + getRefundReason() + "'" +
            ", stripeCheckoutSessionId='" + getStripeCheckoutSessionId() + "'" +
            ", stripePaymentIntentId='" + getStripePaymentIntentId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", ticketType=" + getTicketType() +
            ", user=" + getUser() +
            "}";
    }
}
