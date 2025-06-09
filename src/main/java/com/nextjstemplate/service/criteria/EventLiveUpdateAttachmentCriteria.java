package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventLiveUpdateAttachment} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventLiveUpdateAttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-live-update-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLiveUpdateAttachmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter attachmentType;

    private StringFilter attachmentUrl;

    private IntegerFilter displayOrder;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter liveUpdateId;

    private Boolean distinct;

    public EventLiveUpdateAttachmentCriteria() {}

    public EventLiveUpdateAttachmentCriteria(EventLiveUpdateAttachmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.attachmentType = other.attachmentType == null ? null : other.attachmentType.copy();
        this.attachmentUrl = other.attachmentUrl == null ? null : other.attachmentUrl.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.liveUpdateId = other.liveUpdateId == null ? null : other.liveUpdateId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventLiveUpdateAttachmentCriteria copy() {
        return new EventLiveUpdateAttachmentCriteria(this);
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

    public StringFilter getAttachmentType() {
        return attachmentType;
    }

    public StringFilter attachmentType() {
        if (attachmentType == null) {
            attachmentType = new StringFilter();
        }
        return attachmentType;
    }

    public void setAttachmentType(StringFilter attachmentType) {
        this.attachmentType = attachmentType;
    }

    public StringFilter getAttachmentUrl() {
        return attachmentUrl;
    }

    public StringFilter attachmentUrl() {
        if (attachmentUrl == null) {
            attachmentUrl = new StringFilter();
        }
        return attachmentUrl;
    }

    public void setAttachmentUrl(StringFilter attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public IntegerFilter displayOrder() {
        if (displayOrder == null) {
            displayOrder = new IntegerFilter();
        }
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
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

    public LongFilter getLiveUpdateId() {
        return liveUpdateId;
    }

    public LongFilter liveUpdateId() {
        if (liveUpdateId == null) {
            liveUpdateId = new LongFilter();
        }
        return liveUpdateId;
    }

    public void setLiveUpdateId(LongFilter liveUpdateId) {
        this.liveUpdateId = liveUpdateId;
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
        final EventLiveUpdateAttachmentCriteria that = (EventLiveUpdateAttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(attachmentType, that.attachmentType) &&
            Objects.equals(attachmentUrl, that.attachmentUrl) &&
            Objects.equals(displayOrder, that.displayOrder) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(liveUpdateId, that.liveUpdateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attachmentType, attachmentUrl, displayOrder, createdAt, updatedAt, liveUpdateId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLiveUpdateAttachmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (attachmentType != null ? "attachmentType=" + attachmentType + ", " : "") +
            (attachmentUrl != null ? "attachmentUrl=" + attachmentUrl + ", " : "") +
            (displayOrder != null ? "displayOrder=" + displayOrder + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (liveUpdateId != null ? "liveUpdateId=" + liveUpdateId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
