package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EmailLog} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EmailLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /email-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter recipientEmail;

    private StringFilter subject;

    private ZonedDateTimeFilter sentAt;

    private StringFilter status;

    private StringFilter type;

    private LongFilter campaignId;

    private Boolean distinct;

    public EmailLogCriteria() {}

    public EmailLogCriteria(EmailLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.recipientEmail = other.recipientEmail == null ? null : other.recipientEmail.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.sentAt = other.sentAt == null ? null : other.sentAt.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.campaignId = other.campaignId == null ? null : other.campaignId.copy();
        this.campaignId = other.campaignId == null ? null : other.campaignId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmailLogCriteria copy() {
        return new EmailLogCriteria(this);
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

    public StringFilter getRecipientEmail() {
        return recipientEmail;
    }

    public StringFilter recipientEmail() {
        if (recipientEmail == null) {
            recipientEmail = new StringFilter();
        }
        return recipientEmail;
    }

    public void setRecipientEmail(StringFilter recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public StringFilter subject() {
        if (subject == null) {
            subject = new StringFilter();
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public ZonedDateTimeFilter getSentAt() {
        return sentAt;
    }

    public ZonedDateTimeFilter sentAt() {
        if (sentAt == null) {
            sentAt = new ZonedDateTimeFilter();
        }
        return sentAt;
    }

    public void setSentAt(ZonedDateTimeFilter sentAt) {
        this.sentAt = sentAt;
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getCampaignId() {
        return campaignId;
    }

    public LongFilter campaignId() {
        if (campaignId == null) {
            campaignId = new LongFilter();
        }
        return campaignId;
    }

    public void setCampaignId(LongFilter campaignId) {
        this.campaignId = campaignId;
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
        final EmailLogCriteria that = (EmailLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(recipientEmail, that.recipientEmail) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(sentAt, that.sentAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(type, that.type) &&
            Objects.equals(campaignId, that.campaignId) &&
            Objects.equals(campaignId, that.campaignId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, recipientEmail, subject, sentAt, status, type, campaignId, campaignId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailLogCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (recipientEmail != null ? "recipientEmail=" + recipientEmail + ", " : "") +
            (subject != null ? "subject=" + subject + ", " : "") +
            (sentAt != null ? "sentAt=" + sentAt + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (campaignId != null ? "campaignId=" + campaignId + ", " : "") +
            (campaignId != null ? "campaignId=" + campaignId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
